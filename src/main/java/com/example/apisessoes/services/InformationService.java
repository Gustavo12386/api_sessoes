package com.example.apisessoes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.apisessoes.dto.patient.PatientRegisterRequest;
import com.example.apisessoes.dto.patient.PatientResponse;
import com.example.apisessoes.entities.Especiality;
import com.example.apisessoes.entities.Information;
import com.example.apisessoes.repositories.EspecialityRepository;
import com.example.apisessoes.repositories.InformationRepository;

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
	
	
	@Transactional
    public PatientResponse register(
            PatientRegisterRequest request) {

        if (repository_information.existsByEmail(
                request.email())) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "E-mail já cadastrado"
            );
        }

        Especiality especiality = repository_especialization
            .findById(request.especialityId())
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Especialidade não encontrada"
                )
            );

        Information patient = new Information();

        patient.setName(request.name());
        patient.setPhone(request.phone());
        patient.setEmail(request.email());

        patient.setPassword(
            passwordEncoder.encode(request.password())
        );

        patient.setDate_birth(request.date_birth());
        patient.setEspeciality(especiality);
        patient.setDate_scheduled(request.date_scheduled());
        patient.setHour(request.hour());

        Information saved =
        		repository_information.save(patient);

        return toResponse(saved);
    }
	
	 private PatientResponse toResponse(
	            Information patient) {

	        return new PatientResponse(
	            patient.getId(),
	            patient.getName(),
	            patient.getPhone(),
	            patient.getEmail(),
	            patient.getDate_birth(),
	            patient.getDate_scheduled(),
	            patient.getHour(),
	            patient.getEspeciality().getEspecialization_id()
	        );
	    }
	
	public List<Information> getAllInformations() {
        return repository_information.findAll();
    }

}
