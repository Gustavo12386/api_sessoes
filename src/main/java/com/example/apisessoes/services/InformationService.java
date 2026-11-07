package com.example.apisessoes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;

import com.example.apisessoes.dto.patient.PatientRegisterRequest;

import com.example.apisessoes.entities.Especiality;
import com.example.apisessoes.entities.Information;
import com.example.apisessoes.repositories.EspecialityRepository;
import com.example.apisessoes.repositories.InformationRepository;
import com.example.apisessoes.entities.Date;
import com.example.apisessoes.repositories.DateRepository;
import com.example.apisessoes.repositories.HourRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InformationService {
	
	@Autowired	
	private InformationRepository repository_information;	
	
	@Autowired	
	private EspecialityRepository repository_especialization;	
	
	@Autowired	
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private DateRepository repository_date;
	
	@Autowired
	private HourRepository repository_hour;
	
	
    public Information register(
            PatientRegisterRequest request) {

        if (repository_information.existsByEmail(
                request.email())) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "E-mail já cadastrado"
            );
        }

        Especiality especiality =
	            repository_especialization.findById(request.especialityId())
	                .orElseThrow(() ->
	                    new RuntimeException("Especialidade não encontrada!")
	                );

	    
	        Date date = repository_date
	            .findByEspeciality_EspecializationIdAndCurrentDate(
	            		request.especialityId(),
	            		request.date_scheduled()
	            )
	            .orElseThrow(() ->
	                new RuntimeException(
	                    "Data não disponível para esta especialidade!"
	                )
	            );

	   
	        boolean validHour = repository_hour
	            .findByDate_DateId(date.getDateId())
	            .stream()
	            .anyMatch(hour ->
	                hour.getCurrentHour().equals(request.hour())
	            );

	        if (!validHour) {
	            throw new RuntimeException(
	                "Horário não disponível para esta data!"
	            );
	        }

        Information patient = new Information();

        patient.setName(request.name());
        patient.setEspeciality_name(
	            especiality.getEspecializationName()
	        );

        patient.setPhone(request.phone());
        patient.setEmail(request.email());

        patient.setPassword(
            passwordEncoder.encode(request.password())
        );

        patient.setDate_birth(request.date_birth());
        patient.setEspeciality(especiality);
        patient.setDate_scheduled(request.date_scheduled());
        patient.setHour(request.hour());


        return repository_information.save(patient);
    }
	
	
	
	public List<Information> getAllInformations() {
        return repository_information.findAll();
    }

}
