package com.example.apisessoes.dto.patient;

import java.util.UUID;

public record AppointmentDTO( UUID dateId, UUID hourId, String date_scheduled, String scheduled_hour) {

}
