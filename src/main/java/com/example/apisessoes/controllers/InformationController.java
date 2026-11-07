package com.example.apisessoes.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apisessoes.dto.patient.PatientRegisterRequest;

import com.example.apisessoes.entities.Information;
import com.example.apisessoes.services.InformationService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(value = "/patient")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class InformationController {
	
	@Autowired	
	private InformationService service;	
	
	  @PostMapping("/registersessions")
	    public ResponseEntity<Map<String, Object>> register(
	            @RequestBody PatientRegisterRequest request) {

	        Information information = service.register(request);

	        return ResponseEntity
	            .status(HttpStatus.CREATED)
	            .body(Map.of(
	                "message", "Primeira consulta cadastrada com sucesso!",
	                "id", information.getId()
	            ));
	    }
	
	
	 @GetMapping
	 private ResponseEntity<List<Information >> getInfo(){
	       List<Information> list = service.getAllInformations();
	       return ResponseEntity.ok(list);
	 }
}
