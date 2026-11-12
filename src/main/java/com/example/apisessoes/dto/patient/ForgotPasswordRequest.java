package com.example.apisessoes.dto.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest( @NotBlank @Email String email) {
  
}
