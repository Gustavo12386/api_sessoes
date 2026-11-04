package com.example.apisessoes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apisessoes.entities.Especiality;
import com.example.apisessoes.repositories.EspecialityRepository;

import java.util.List;

@Service
public class EspecialityService {
	
	@Autowired	
	private EspecialityRepository repos;
	
	public List<Especiality> getAllEspecialities() {
        return repos.findAll();
    }
}
