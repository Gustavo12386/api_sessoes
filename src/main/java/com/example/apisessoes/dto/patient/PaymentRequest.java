package com.example.apisessoes.dto.patient;

import java.util.UUID;

public record PaymentRequest(UUID informationId,
		String patientName,
	    String couponCode,
	    String paymentMethod) {

}
