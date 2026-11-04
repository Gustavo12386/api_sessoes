package com.example.apisessoes.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apisessoes.entities.Date;
import com.example.apisessoes.services.DateService;

@RestController
@RequestMapping(value = "/date")
public class DateController {
	
	@Autowired	
	private DateService service_date;	
	
	@GetMapping
	private ResponseEntity<List<Date>> getDate(){
	       List<Date> list = service_date.getAllDates();
	       return ResponseEntity.ok(list);
	}
	
	 @GetMapping("/{dateId}")
	 public ResponseEntity<List<Date>> listarPorUsuario(@PathVariable UUID dateId) {
	        List<Date> dates = service_date.getDateById(dateId);
	        return ResponseEntity.ok(dates);
	 }
}
