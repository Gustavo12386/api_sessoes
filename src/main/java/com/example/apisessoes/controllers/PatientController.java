package com.example.apisessoes.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apisessoes.dto.patient.AppointmentResponse;
import com.example.apisessoes.dto.patient.PatientMeResponse;
import com.example.apisessoes.services.AppointmentService;
import com.example.apisessoes.services.InformationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
	
	@Autowired
	private InformationService informationService;
	
	@Autowired
	private AppointmentService appointmentService;

    @GetMapping("/me")
    public ResponseEntity<PatientMeResponse> getAuthenticatedPatient(
            Authentication authentication) {

        String email = authentication.getName();

        PatientMeResponse patient =
                informationService.getPatientByEmail(email);

        return ResponseEntity.ok(patient);
    }
    
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> getAppointments(
            Authentication authentication) {

        String email = authentication.getName();

        List<AppointmentResponse> appointments =
            appointmentService.getAppointmentsByPatient(email);

        return ResponseEntity.ok(appointments);
    }
}
