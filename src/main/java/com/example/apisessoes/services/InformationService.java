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
	private AppointmentService appointmentService;
	
	
    public Information register(
            PatientRegisterRequest request) {
    	   Especiality especiality =
    			   repository_especialization.findById(request.especialityId())
    	                .orElseThrow(() ->
    	                    new RuntimeException(
    	                        "Especialidade não encontrada!"
    	                    )
    	                );

    	        Information patient = new Information();

    	        patient.setEspeciality(especiality);
    	        patient.setEspeciality_name(
    	            especiality.getEspecializationName()
    	        );

    	        patient.setName(request.name());
    	        patient.setPhone(request.phone());
    	        patient.setEmail(request.email());
    	        patient.setPassword(
    	                passwordEncoder.encode(request.password())
    	        );
    	        patient.setDate_birth(request.date_birth());    	       

    	        Information savedPatient =
    	        		repository_information.save(patient);

    	        appointmentService.saveAppointments(
    	            savedPatient,
    	            request.especialityId(),
    	            request.appointments()
    	        );

    	        return savedPatient;
    }
	
	
	
	public List<Information> getAllInformations() {
        return repository_information.findAll();
    }

}
