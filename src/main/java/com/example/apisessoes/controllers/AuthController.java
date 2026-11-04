package com.example.apisessoes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apisessoes.dto.auth.LoginRequest;
import com.example.apisessoes.dto.auth.LoginResponse;
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
}
