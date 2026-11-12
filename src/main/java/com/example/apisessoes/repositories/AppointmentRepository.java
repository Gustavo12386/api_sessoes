package com.example.apisessoes.repositories;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.apisessoes.entities.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
	 boolean existsByHour_HourId(UUID hourId);

	 List<Appointment> findByPatient_Id(UUID patientId);
	 List<Appointment> findByDate_DateId(UUID dateId);
	 @Query("""
		        SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
		        FROM Appointment a
		        WHERE a.date_scheduled = :date
		        AND a.scheduled_hour = :hour
		    """)
		    boolean existsScheduledAppointment(
		        @Param("date") LocalDate date,
		        @Param("hour") LocalTime hour
		    );
	 List<Appointment> findByPatient_Email(String email);
	 Optional<Appointment> findByAppointmentIdAndPatient_Email(
			    UUID appointmentId,
			    String email
			);
}
