package com.example.apisessoes.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apisessoes.entities.Hour;
import com.example.apisessoes.repositories.HourRepository;
import com.example.apisessoes.repositories.AppointmentRepository;
import com.example.apisessoes.repositories.DateRepository;
import com.example.apisessoes.entities.Date;

@Service
public class HourService {
	@Autowired	
	private HourRepository repos;
	
	@Autowired
	private DateRepository dateRepository;
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	public List<Hour> getAllHours() {
        return repos.findAll();
    }
	
	 public List<Hour> getHoursByDate(UUID especialityId, UUID dateId) {

	        Date date = dateRepository.findById(dateId)
	            .orElseThrow(() ->
	                new RuntimeException("Data não encontrada!")
	            );

	        if (!date.getEspeciality()
	                .getEspecializationId()
	                .equals(especialityId)) {
	            throw new RuntimeException(
	                "Esta data não pertence à especialidade selecionada!"
	            );
	        }

	        return repos.findByDate_DateId(dateId)
	                .stream()
	                .filter(hour ->
	                    !appointmentRepository.existsByHour_HourId(
	                        hour.getHourId()
	                    )
	                )
	                .toList();
	    }
}
