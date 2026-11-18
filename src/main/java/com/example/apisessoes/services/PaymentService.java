package com.example.apisessoes.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.apisessoes.config.MercadoPagoProperties;
import com.example.apisessoes.repositories.CouponRepository;
import com.example.apisessoes.repositories.InformationRepository;
import com.example.apisessoes.repositories.PaymentRepository;
import com.example.apisessoes.dto.patient.PaymentRequest;
import com.example.apisessoes.dto.patient.PaymentResponse;
import com.example.apisessoes.entities.Coupon;
import com.example.apisessoes.entities.Information;
import com.example.apisessoes.entities.Payment;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class PaymentService {
	private final PaymentRepository paymentRepository;
	 private final InformationRepository informationRepository;
	 private final CouponRepository couponRepository;
	 private final MercadoPagoProperties mercadoPagoProperties;
	 private final WebClient.Builder webClientBuilder;

	private final BigDecimal consultationAmount =
	new BigDecimal("100.00");
	
	public BigDecimal getConsultationAmount() {
       return consultationAmount;
   }
	
	  public void processWebhookNotification(String paymentId) {

	        if (paymentId == null || paymentId.isBlank()) {
	            throw new IllegalArgumentException("ID do pagamento não informado.");
	        }

	        JsonNode paymentData;

	        try {
	            paymentData = webClientBuilder.build()
	                .get()
	                .uri("https://api.mercadopago.com/v1/payments/{id}", paymentId)
	                .header(
	                    "Authorization",
	                    "Bearer " + mercadoPagoProperties.getAccessToken()
	                )
	                .retrieve()
	                .bodyToMono(JsonNode.class)
	                .block();

	        } catch (WebClientResponseException e) {
	            throw new IllegalStateException(
	                "Erro ao consultar pagamento no Mercado Pago: "
	                + e.getStatusCode()
	            );
	        }

	        if (paymentData == null) {
	            throw new IllegalStateException(
	                "O Mercado Pago não retornou os dados do pagamento."
	            );
	        }

	        String externalReference =
	            paymentData.path("external_reference").asText(null);

	        String status =
	            paymentData.path("status").asText(null);

	        String mercadoPagoId =
	            paymentData.path("id").asText(null);

	        BigDecimal transactionAmount =
	            paymentData.hasNonNull("transaction_amount")
	                ? paymentData.get("transaction_amount").decimalValue()
	                : null;

	        if (externalReference == null || externalReference.isBlank()) {
	            throw new IllegalStateException(
	                "Pagamento sem external_reference."
	            );
	        }

	        if (status == null || mercadoPagoId == null) {
	            throw new IllegalStateException(
	                "Dados incompletos retornados pelo Mercado Pago."
	            );
	        }

	        updatePaymentStatus(
	            mercadoPagoId,
	            externalReference,
	            status,
	            transactionAmount
	        );
	    }
	  
	  @Transactional
	    protected void updatePaymentStatus(
	        String mercadoPagoId,
	        String externalReference,
	        String mercadoPagoStatus,
	        BigDecimal transactionAmount
	    ) {

	        Payment payment = paymentRepository
	            .findByExternalReference(externalReference)
	            .orElseThrow(() -> new IllegalArgumentException(
	                "Pagamento não encontrado para a referência: "
	                + externalReference
	            ));

	        // Confere se o valor pago corresponde ao valor esperado.
	        if (transactionAmount == null ||
	            transactionAmount.compareTo(payment.getFinalAmount()) != 0) {

	            throw new IllegalStateException(
	                "O valor retornado pelo Mercado Pago não corresponde "
	                + "ao valor esperado."
	            );
	        }

	        // Evita processar novamente uma confirmação já registrada.
	        boolean alreadyApproved =
	            "APPROVED".equalsIgnoreCase(payment.getStatus());

	        payment.setMercadoPagoPaymentId(mercadoPagoId);

	        switch (mercadoPagoStatus.toLowerCase()) {

	            case "approved" -> {
	                payment.setStatus("APPROVED");

	                if (!alreadyApproved) {
	                    incrementCouponUsage(payment);
	                }
	            }

	            case "pending", "in_process", "authorized" ->
	                payment.setStatus("PENDING");

	            case "rejected" ->
	                payment.setStatus("REJECTED");

	            case "cancelled" ->
	                payment.setStatus("CANCELLED");

	            case "refunded" ->
	                payment.setStatus("REFUNDED");

	            case "charged_back" ->
	                payment.setStatus("CHARGED_BACK");

	            default ->
	                payment.setStatus("UNKNOWN");
	        }

	        paymentRepository.save(payment);
	    }
 
	  private void incrementCouponUsage(Payment payment) {

		    if (payment.getCouponCode() == null ||
		        payment.getCouponCode().isBlank()) {
		        return;
		    }

		    Coupon coupon = couponRepository
		        .findByCouponCode(payment.getCouponCode())
		        .orElseThrow(() -> new IllegalStateException(
		            "Cupom não encontrado: " + payment.getCouponCode()
		        ));

		    coupon.setUsesCount(coupon.getUsesCount() + 1);

		    couponRepository.save(coupon);
		}
	
	@Transactional
   public PaymentResponse createCheckout(PaymentRequest request) {

       // 1. Buscar cadastro do paciente
       Information information = informationRepository
               .findById(request.informationId())
               .orElseThrow(() ->
                   new RuntimeException("Cadastro não encontrado.")
               );

       // 2. Definir valor original
       BigDecimal originalAmount = consultationAmount;

       BigDecimal discountAmount = BigDecimal.ZERO;
       String couponCode = null;

       // 3. Validar cupom
       if (request.couponCode() != null &&
               !request.couponCode().isBlank()) {

           Coupon coupon = couponRepository
                   .findByCouponCode(
                       request.couponCode().trim().toUpperCase()
                   )
                   .orElseThrow(() ->
                       new RuntimeException("Cupom não encontrado.")
                   );

           LocalDateTime now = LocalDateTime.now();

           if (!Boolean.TRUE.equals(coupon.getActive())) {
               throw new RuntimeException("Cupom inativo.");
           }

           if (coupon.getValidFrom() != null &&
                   now.isBefore(coupon.getValidFrom())) {
               throw new RuntimeException("Cupom ainda não está válido.");
           }

           if (coupon.getValidUntil() != null &&
                   now.isAfter(coupon.getValidUntil())) {
               throw new RuntimeException("Cupom expirado.");
           }

           if (coupon.getMaxUses() != null &&
                   coupon.getUsesCount() >= coupon.getMaxUses()) {
               throw new RuntimeException("Limite de utilização atingido.");
           }

           BigDecimal percentage = coupon.getDiscountPercentage();

           if (percentage.compareTo(BigDecimal.ZERO) < 0 ||
                   percentage.compareTo(new BigDecimal("100")) > 0) {
               throw new RuntimeException("Percentual de desconto inválido.");
           }

           discountAmount = originalAmount
                   .multiply(percentage)
                   .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

           couponCode = coupon.getCouponCode();
       }

       // 4. Calcular valor final
       BigDecimal finalAmount = originalAmount.subtract(discountAmount);

       if (finalAmount.compareTo(BigDecimal.ZERO) <= 0) {
           throw new RuntimeException("Valor do pagamento inválido.");
       }

       // 5. Criar referência externa
       String externalReference = UUID.randomUUID().toString();

       // 6. Salvar pagamento pendente
       Payment payment = new Payment();

       payment.setInformation(information);
       payment.setExternalReference(externalReference);
       payment.setOriginalAmount(originalAmount);
       payment.setDiscountAmount(discountAmount);
       payment.setFinalAmount(finalAmount);
       payment.setCouponCode(couponCode);
       payment.setPaymentMethod(request.paymentMethod());
       payment.setStatus("PENDING");
       payment.setCreatedAt(LocalDateTime.now());

       payment = paymentRepository.save(payment);

       // 7. Criar preferência no Mercado Pago
       WebClient client = webClientBuilder
               .baseUrl("https://api.mercadopago.com")
               .defaultHeader(
                   HttpHeaders.AUTHORIZATION,
                   "Bearer " + mercadoPagoProperties.getAccessToken()
               )
               .build();

       Map<String, Object> item = Map.of(
           "title", "Primeira consulta",
           "quantity", 1,
           "unit_price", finalAmount.doubleValue(),
           "currency_id", "BRL"
       );

       Map<String, Object> preference = new HashMap<>();

       preference.put("items", List.of(item));
       preference.put("external_reference", externalReference);
       preference.put("payer", Map.of(
           "name", information.getName(),
           "email", information.getEmail()
       ));

       preference.put("back_urls", Map.of(
           "success", "https://seu-frontend.com/pagamento/sucesso",
           "failure", "https://seu-frontend.com/pagamento/falha",
           "pending", "https://seu-frontend.com/pagamento/pendente"
       ));

       preference.put("auto_return", "approved");

       Map response = client.post()
               .uri("/checkout/preferences")
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(preference)
               .retrieve()
               .bodyToMono(Map.class)
               .block();

       if (response == null || response.get("id") == null) {
           throw new RuntimeException(
               "Não foi possível criar o checkout no Mercado Pago."
           );
       }

       String preferenceId = response.get("id").toString();

       String checkoutUrl = response.get("init_point").toString();

       // Atualizar pagamento com os dados do checkout
       payment.setPreferenceId(preferenceId);

       paymentRepository.save(payment);

       return new PaymentResponse(
           payment.getId(),
           preferenceId,
           payment.getInformation().getName(),
           checkoutUrl,
           originalAmount,
           discountAmount,
           finalAmount,
           payment.getStatus()
       );
   }
}
