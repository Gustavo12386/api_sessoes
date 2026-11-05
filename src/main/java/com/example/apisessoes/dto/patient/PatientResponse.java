package com.example.apisessoes.dto.patient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record PatientResponse( 
		UUID id,
		String especiality_name,
	    String name,
	    String phone,
	    String email,
	    LocalDate date_birth,
	    LocalDate date_scheduled,
	    LocalTime hour,
	    UUID especialityId) {

}
