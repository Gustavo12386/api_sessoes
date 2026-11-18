package com.example.apisessoes.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apisessoes.dto.patient.PaymentRequest;
import com.example.apisessoes.dto.patient.PaymentResponse;
import com.example.apisessoes.services.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {
	@Autowired
	private PaymentService paymentService;
	
	 @PostMapping("/checkout")
	 public ResponseEntity<PaymentResponse> createCheckout(
	     @RequestBody PaymentRequest request
	 ) {

	 PaymentResponse response =
	 paymentService.createCheckout(request);

	   return ResponseEntity.ok(response);
	 }
	 
	 @GetMapping("/consultation-amount")
	    public ResponseEntity<BigDecimal> getConsultationAmount() {

	        return ResponseEntity.ok(
	            paymentService.getConsultationAmount()
	        );
	}
	 
	 @GetMapping
	 public ResponseEntity<List<PaymentResponse>> getAllPayments() {
	     return ResponseEntity.ok(paymentService.findAllPayments());
	 }  
	 
}
