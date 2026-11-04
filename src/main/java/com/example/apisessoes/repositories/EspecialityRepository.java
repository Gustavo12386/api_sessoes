package com.example.apisessoes.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.apisessoes.entities.Especiality;

public interface EspecialityRepository extends JpaRepository<Especiality, UUID> {
	 
}
