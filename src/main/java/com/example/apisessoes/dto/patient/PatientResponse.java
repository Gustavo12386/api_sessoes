package com.example.apisessoes.dto.patient;

import java.time.LocalDate;
import java.util.UUID;

public record PatientResponse( 
		UUID id,
	    String name,
	    String phone,
	    String email,
	    LocalDate date_birth,
	    LocalDate date_scheduled,
	    String hour,
	    UUID especialityId) {

}
