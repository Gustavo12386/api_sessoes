package com.example.apisessoes.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.apisessoes.dto.patient.AppointmentDTO;
import com.example.apisessoes.dto.patient.AppointmentResponse;
import com.example.apisessoes.entities.Appointment;
import com.example.apisessoes.entities.Date;
import com.example.apisessoes.entities.Hour;
import com.example.apisessoes.entities.Information;
import com.example.apisessoes.repositories.AppointmentRepository;
import com.example.apisessoes.repositories.DateRepository;
import com.example.apisessoes.repositories.HourRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {
	@Autowired
	private AppointmentRepository appointmentRepository;
	@Autowired
    private DateRepository dateRepository;
	@Autowired
    private HourRepository hourRepository;
	
	 public List<Appointment> saveAppointments(
	            Information patient,
	            UUID especialityId,
	            List<AppointmentDTO> appointmentsDTO) {

	        if (appointmentsDTO == null || appointmentsDTO.isEmpty()) {
	            throw new RuntimeException(
	                "Selecione pelo menos um agendamento!"
	            );
	        }

	        Set<UUID> selectedHours = new HashSet<>();
	        List<Appointment> appointments = new ArrayList<>();

	        for (AppointmentDTO dto : appointmentsDTO) {

	            if (!selectedHours.add(dto.hourId())) {
	                throw new RuntimeException(
	                    "O mesmo horário foi selecionado mais de uma vez!"
	                );
	            }

	            Date date = dateRepository.findById(dto.dateId())
	                .orElseThrow(() ->
	                    new RuntimeException("Data não encontrada!")
	                );

	            if (!date.getEspeciality()
	                    .getEspecializationId()
	                    .equals(especialityId)) {
	                throw new RuntimeException(
	                    "A data não pertence à especialidade selecionada!"
	                );
	            }

	            Hour hour = hourRepository.findById(dto.hourId())
	                .orElseThrow(() ->
	                    new RuntimeException("Horário não encontrado!")
	                );

	            if (!hour.getDate().getDateId().equals(date.getDateId())) {
	                throw new RuntimeException(
	                    "O horário não pertence à data selecionada!"
	                );
	            }

	            if (appointmentRepository.existsByHour_HourId(
	                    hour.getHourId())) {
	                throw new RuntimeException(
	                    "Um dos horários selecionados já foi reservado!"
	                );
	            }

	            Appointment appointment = new Appointment();

	            appointment.setPatient(patient);
	            appointment.setDate(date);
	            appointment.setHour(hour);
	            appointment.setDate_scheduled(date.getCurrentDate());
	            appointment.setScheduled_hour(hour.getCurrentHour());

	            appointments.add(appointment);
	        }

	        return appointmentRepository.saveAll(appointments);
	    }
	 
	 public List<AppointmentResponse> getAppointmentsByPatient(
		        String email) {

		    return appointmentRepository.findByPatient_Email(email)
		        .stream()
		        .map(appointment -> new AppointmentResponse(
		            appointment.getAppointmentId(),
		            appointment.getDate_scheduled(),
		            appointment.getScheduled_hour()
		        ))
		        .toList();
	}
	 
	 @Transactional
	 public void deleteAppointment(UUID appointmentId, String email) {

	     Appointment appointment = appointmentRepository
	         .findByAppointmentIdAndPatient_Email(appointmentId, email)
	         .orElseThrow(() -> new ResponseStatusException(
	             HttpStatus.NOT_FOUND,
	             "Agendamento não encontrado!"
	         ));

	     appointmentRepository.delete(appointment);
	 } 

}
