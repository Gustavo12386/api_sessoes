package com.example.apisessoes.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apisessoes.entities.Date;
import com.example.apisessoes.repositories.AppointmentRepository;
import com.example.apisessoes.repositories.DateRepository;
import com.example.apisessoes.repositories.HourRepository;
import com.example.apisessoes.repositories.InformationRepository;


@Service
public class DateService {
	@Autowired	
	private DateRepository dateRepository;
	@Autowired
	private HourRepository hourRepository;
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	public List<Date> getAllDates() {
        return dateRepository.findAll();
    }
	
	 public List<Date> getDatesByEspeciality(UUID especialityId) {

	        List<Date> dates =
	            dateRepository.findByEspeciality_EspecializationId(especialityId);

	        return dates.stream()
	                .filter(date ->
	                    hourRepository.findByDate_DateId(date.getDateId())
	                        .stream()
	                        .anyMatch(hour ->
	                            !appointmentRepository.existsScheduledAppointment(
	                                date.getCurrentDate(),
	                                hour.getCurrentHour()
	                            )
	                        )
	                )
	            .toList();
	    }
	
	
}
