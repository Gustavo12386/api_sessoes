package com.example.apisessoes.services;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class MercadoPagoService {
	private final RestClient restClient;

	 @Value("${app.frontend-url}")
	 private String frontendUrl;
	 
	 public MercadoPagoService(RestClient mercadoPagoRestClient) {
	        this.restClient = mercadoPagoRestClient;
	    }

	    public String createCheckout(
	        UUID paymentId,
	        String patientEmail,
	        String patientName,
	        String description,
	        double amount,
	        String paymentMethod
	    ) {

	        String[] names = patientName.trim().split("\\s+", 2);

	        Map<String, Object> payer = Map.of(
	            "email", patientEmail,
	            "first_name", names[0],
	            "last_name", names.length > 1 ? names[1] : names[0]
	        );

	        Map<String, Object> item = Map.of(
	            "title", description,
	            "quantity", 1,
	            "unit_price", String.format(
	                java.util.Locale.US,
	                "%.2f",
	                amount
	            )
	        );

	        Map<String, Object> paymentConfig = Map.of(
	            "max_installments", 12
	        );

	        Map<String, Object> onlineConfig = Map.of(
	            "success_url", frontendUrl + "/pagamento/sucesso",
	            "failure_url", frontendUrl + "/pagamento/falha",
	            "pending_url", frontendUrl + "/pagamento/pendente",
	            "auto_return", "approved"
	        );

	        Map<String, Object> config = Map.of(
	            "online", onlineConfig,
	            "payment_method", paymentConfig
	        );

	        Map<String, Object> body = Map.of(
	            "type", "online",
	            "total_amount", String.format(
	                java.util.Locale.US,
	                "%.2f",
	                amount
	            ),
	            "external_reference", paymentId.toString(),
	            "processing_mode", "manual",
	            "capture_mode", "automatic_async",
	            "payer", payer,
	            "items", new Object[]{item},
	            "config", config
	        );

	        Map response = restClient.post()
	            .uri("/v1/orders")
	            .header("X-Idempotency-Key", UUID.randomUUID().toString())
	            .body(body)
	            .retrieve()
	            .body(Map.class);

	        if (response == null || response.get("checkout_url") == null) {
	            throw new IllegalStateException(
	                "O Mercado Pago não retornou a URL de pagamento."
	            );
	        }

	        return response.get("checkout_url").toString();
	    }
	 
}
