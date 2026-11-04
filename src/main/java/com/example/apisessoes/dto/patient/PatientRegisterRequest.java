package com.example.apisessoes.dto.patient;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PatientRegisterRequest(
		@NotBlank
	    String name,

	    @NotBlank
	    String phone,

	    @NotBlank
	    @Email
	    String email,

	    @NotBlank
	    @Size(min = 8)
	    String password,

	    @NotNull
	    LocalDate date_birth,

	    @NotNull
	    UUID especialityId,

	    LocalDate date_scheduled,

	    String hour) {

}
