package com.example.apisessoes.dto.patient;

import java.time.LocalDate;
import java.util.UUID;

public record PatientMeResponse(
		  UUID id,
		  String name,
		  String email,
		  String phone,
		  LocalDate date_birth,
		  String especiality_name
) {

}
