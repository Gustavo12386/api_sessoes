package com.example.apisessoes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apisessoes.dto.InformationDTO;
import com.example.apisessoes.entities.Especiality;
import com.example.apisessoes.entities.Information;
import com.example.apisessoes.repositories.EspecialityRepository;
import com.example.apisessoes.repositories.InformationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InformationService {
	
	@Autowired	
	private InformationRepository repository_information;	
	
	@Autowired	
	private EspecialityRepository repository_especialization;	
	
	public Information save(InformationDTO dto) {

       Especiality especiality = repository_especialization.findById(dto.especialityId())
               .orElseThrow(() -> new RuntimeException("Categoria não encontrada!"));

  
       Information information = new Information();
       information.setEspeciality(especiality);
       information.setName(dto.name());
       information.setPhone(dto.phone()); 
       information.setEmail(dto.email()); 
       information.setDate_birth(dto.date_birth()); 
       information.setDate_scheduled(dto.date_scheduled()); 
       information.setHour(dto.hour()); 
       information.setQuestion1(dto.question1());
       information.setQuestion1(dto.question2());
       information.setQuestion1(dto.question3());
       
       return repository_information.save(information);
    }
	
	public List<Information> getAllInformations() {
        return repository_information.findAll();
    }

}
