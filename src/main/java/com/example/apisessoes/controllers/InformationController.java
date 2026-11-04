package com.example.apisessoes.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apisessoes.dto.patient.PatientRegisterRequest;
import com.example.apisessoes.dto.patient.PatientResponse;
import com.example.apisessoes.entities.Information;
import com.example.apisessoes.services.InformationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(value = "/information")
@RequiredArgsConstructor
public class InformationController {
	
	@Autowired	
	private InformationService service_information;	
	
	@PostMapping("/register")
    public ResponseEntity<PatientResponse> register(
            @Valid
            @RequestBody
            PatientRegisterRequest request) {

        PatientResponse response =
        		service_information.register(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }
	
	 @GetMapping
	 private ResponseEntity<List<Information >> getInfo(){
	       List<Information> list = service_information.getAllInformations();
	       return ResponseEntity.ok(list);
	 }
}
