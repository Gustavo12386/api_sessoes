package com.example.apisessoes.dto.patient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponse(
		UUID appointmentId,
	    LocalDate date,
	    LocalTime hour) {

}
