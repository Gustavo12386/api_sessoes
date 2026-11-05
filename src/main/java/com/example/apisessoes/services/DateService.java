package com.example.apisessoes.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apisessoes.entities.Date;
import com.example.apisessoes.repositories.DateRepository;


@Service
public class DateService {
	@Autowired	
	private DateRepository repos;
	
	public List<Date> getAllDates() {
        return repos.findAll();
    }
	
	
}
