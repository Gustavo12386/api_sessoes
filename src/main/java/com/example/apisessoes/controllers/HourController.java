package com.example.apisessoes.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apisessoes.entities.Hour;
import com.example.apisessoes.services.HourService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/especialities")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class HourController {
	@Autowired	
	private HourService service;	
	
	 @GetMapping("/{especialityId}/dates/{dateId}/hours")
	    public List<Hour> getHours(
	        @PathVariable UUID especialityId,
	        @PathVariable UUID dateId
	    ) {
	        return service.getHoursByDate(especialityId, dateId);
	    }
}
