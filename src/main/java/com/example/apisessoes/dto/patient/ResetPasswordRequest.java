package com.example.apisessoes.dto.patient;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordRequest(@NotNull UUID patientId,
	    @NotBlank String newPassword) {

}
