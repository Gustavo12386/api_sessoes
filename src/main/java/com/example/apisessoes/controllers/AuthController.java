package com.example.apisessoes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apisessoes.dto.auth.LoginRequest;
import com.example.apisessoes.dto.auth.LoginResponse;
import com.example.apisessoes.dto.patient.ForgotPasswordRequest;
import com.example.apisessoes.dto.patient.ForgotPasswordResponse;
import com.example.apisessoes.dto.patient.ResetPasswordRequest;
import com.example.apisessoes.services.AuthService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	 
	 @Autowired
	 private AuthService authService;
	 
	 
	   @PostMapping("/login")
	    public ResponseEntity<LoginResponse> login(
	            @Valid
	            @RequestBody
	            LoginRequest request) {

	        return ResponseEntity.ok(
	            authService.login(request)
	        );
	    }
	   
	   @GetMapping("/test")
	    public ResponseEntity<String> test() {
	        return ResponseEntity.ok(
	            "Paciente autenticado com sucesso!"
	        );
	    }
	   
	   @PostMapping("/forgot-password")
	   public ResponseEntity<ForgotPasswordResponse> forgotPassword(
	           @RequestBody @Valid ForgotPasswordRequest request) {

	       ForgotPasswordResponse response =
	           authService.findPatientForRecovery(request.email());

	       return ResponseEntity.ok(response);
	   }

	   @PostMapping("/reset-password")
	   public ResponseEntity<Void> resetPassword(
	           @RequestBody @Valid ResetPasswordRequest request) {

	       authService.resetPassword(request);

	       return ResponseEntity.noContent().build();
	   }

}
