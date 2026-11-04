package com.example.apisessoes.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apisessoes.entities.Especiality;
import com.example.apisessoes.services.EspecialityService;

@RestController
@RequestMapping(value = "/especialization")
public class EspecialityController {
	
	@Autowired	
	private EspecialityService service_especiality;	
	
	 @GetMapping
	 private ResponseEntity<List<Especiality >> getEsp(){
	       List<Especiality> list = service_especiality.getAllEspecialities();
	       return ResponseEntity.ok(list);
	 }
}
