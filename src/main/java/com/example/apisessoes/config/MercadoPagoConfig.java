package com.example.apisessoes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class MercadoPagoConfig {
	 @Bean
	    public RestClient mercadoPagoRestClient(
	        @Value("${mercadopago.access-token}") String accessToken
	    ) {

	        return RestClient.builder()
	            .baseUrl("https://api.mercadopago.com")
	            .defaultHeader(
	                "Authorization",
	                "Bearer " + accessToken
	            )
	            .defaultHeader(
	                "Content-Type",
	                "application/json"
	            )
	            .build();
	    }
}
