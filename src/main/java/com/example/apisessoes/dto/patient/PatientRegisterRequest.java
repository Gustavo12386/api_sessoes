package com.example.apisessoes.dto.patient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PatientRegisterRequest(
		UUID especialityId,
	    String especiality_name,
	    String name,
	    String phone,
	    String email,
	    String password,
	    LocalDate date_birth,	    
	    List<AppointmentDTO> appointments
	    ) {

}
