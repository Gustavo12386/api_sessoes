package com.example.apisessoes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoProperties {
	 @Value("${mercadopago.access-token}")
	    private String accessToken;

	 @Value("${mercadopago.webhook-secret:}")
	 private String webhookSecret;

	 public String getAccessToken() {
	      return accessToken;
	 }

	 public String getWebhookSecret() {
	    return webhookSecret;
	 }
}
