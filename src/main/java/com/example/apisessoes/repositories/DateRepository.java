package com.example.apisessoes.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.apisessoes.entities.Date;

public interface DateRepository extends JpaRepository<Date, UUID>  {
	List<Date> findByUsuarioId(UUID dateId);
}
