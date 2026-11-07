package com.example.apisessoes.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.apisessoes.entities.Information;


public interface InformationRepository extends JpaRepository<Information, UUID> {
	 Optional<Information> findByEmail(String email);
     boolean existsByEmail(String email);
     @Query("""
 	        SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END
 	        FROM Information i
 	        WHERE i.especiality.especializationId = :especialityId
 	        AND i.date_scheduled = :date
 	        AND i.hour = :hour
 	    """)
 	    boolean existsScheduledAppointment(
 	        @Param("especialityId") UUID especialityId,
 	        @Param("date") LocalDate date,
 	        @Param("hour") LocalTime hour
 	    );
}
