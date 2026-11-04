package com.example.apisessoes.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.apisessoes.entities.Information;


public interface InformationRepository extends JpaRepository<Information, UUID> {
	 Optional<Information> findByEmail(String email);
     boolean existsByEmail(String email);
}
