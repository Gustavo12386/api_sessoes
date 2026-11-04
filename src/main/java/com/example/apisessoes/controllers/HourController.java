package com.example.apisessoes.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apisessoes.entities.Hour;
import com.example.apisessoes.services.HourService;

@RestController
@RequestMapping(value = "/hour")
public class HourController {
	@Autowired	
	private HourService service_hour;	
	
	@GetMapping
	 private ResponseEntity<List<Hour >> getHour(){
	       List<Hour> list = service_hour.getAllHours();
	       return ResponseEntity.ok(list);
	 }
}
