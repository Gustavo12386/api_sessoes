package com.example.apisessoes.dto.patient;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponse(UUID paymentId,    

	    String preferenceId,
	    
	    String patientName,

	    String checkoutUrl,

	    BigDecimal originalAmount,

	    BigDecimal discountAmount,

	    BigDecimal finalAmount,

	    String status) {

}
