package com.example.apisessoes.repositories;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.apisessoes.entities.Date;

public interface DateRepository extends JpaRepository<Date, UUID>  {
	 List<Date> findByEspeciality_EspecializationId(UUID especialityId);

	    Optional<Date> findByEspeciality_EspecializationIdAndCurrentDate(
	        UUID especialityId,
	        LocalDate currentDate
	    );
}
