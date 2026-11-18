package com.example.apisessoes.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.apisessoes.config.MercadoPagoProperties;
import com.example.apisessoes.services.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class WebhookController {
	private final MercadoPagoProperties mercadoPagoProperties;
    private final PaymentService paymentService;

    @PostMapping("/mercadopago")
    public ResponseEntity<Void> receiveNotification(
    		 @RequestHeader(value = "x-signature", required = false) String xSignature,
    	        @RequestHeader(value = "x-request-id", required = false) String xRequestId,
    	        @RequestParam(value = "data.id", required = false) String dataId,
    	        @RequestParam(value = "type", required = false) String type) {

    	    if (!validateSignature(xSignature, xRequestId, dataId)) {
    	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	    }

    	    if (!"payment".equals(type) || dataId == null || dataId.isBlank()) {
    	        return ResponseEntity.ok().build();
    	    }

    	    paymentService.processWebhookNotification(dataId);

    	    return ResponseEntity.ok().build();
    }

    private boolean validateSignature(
            String xSignature,
            String xRequestId,
            String dataId) {

        String secret = mercadoPagoProperties.getWebhookSecret();

        if (secret == null || secret.isBlank()
                || xSignature == null || xRequestId == null
                || dataId == null) {
            return false;
        }

        try {
            String timestamp = null;
            String receivedHash = null;

            for (String part : xSignature.split(",")) {
                String[] entry = part.trim().split("=", 2);

                if (entry.length != 2) {
                    continue;
                }

                if ("ts".equals(entry[0])) {
                    timestamp = entry[1];
                } else if ("v1".equals(entry[0])) {
                    receivedHash = entry[1];
                }
            }

            if (timestamp == null || receivedHash == null) {
                return false;
            }

            String manifest = "id:" + dataId.toLowerCase()
                    + ";request-id:" + xRequestId
                    + ";ts:" + timestamp + ";";

            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");

            javax.crypto.spec.SecretKeySpec key =
                    new javax.crypto.spec.SecretKeySpec(
                            secret.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                            "HmacSHA256"
                    );

            mac.init(key);

            byte[] hash = mac.doFinal(
                    manifest.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );

            StringBuilder generatedHash = new StringBuilder();

            for (byte b : hash) {
                generatedHash.append(String.format("%02x", b));
            }

            return java.security.MessageDigest.isEqual(
                    generatedHash.toString().getBytes(
                            java.nio.charset.StandardCharsets.UTF_8
                    ),
                    receivedHash.getBytes(
                            java.nio.charset.StandardCharsets.UTF_8
                    )
            );

        } catch (Exception e) {
            return false;
        }
    }
}
