package com.example.apisessoes.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.apisessoes.entities.Information;
import com.example.apisessoes.repositories.InformationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientUserDetailsService implements UserDetailsService {
    
	 @Autowired
	 private InformationRepository repository;

	 @Override
	 public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		 Information patient = repository
		            .findByEmail(email)
		            .orElseThrow(() ->
		                new UsernameNotFoundException(
		                    "Paciente não encontrado"
		                )
		            );

		        return User.builder()
		            .username(patient.getEmail())
		            .password(patient.getPassword())
		            .roles("PATIENT")
		            .build();
	 }
	 
	 
}
