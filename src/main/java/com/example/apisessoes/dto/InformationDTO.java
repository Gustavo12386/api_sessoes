package com.example.apisessoes.dto;

import java.time.LocalDate;
import java.util.UUID;

public record InformationDTO(UUID especialityId, String name, String phone, String email,
		LocalDate  date_birth, LocalDate date_scheduled, String hour, String question1,
		String question2, String question3) {

}
