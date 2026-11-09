package com.example.apisessoes.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_appointments")
public class Appointment {
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "appointment_id")
    private UUID appointmentId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Information patient;

    @ManyToOne
    @JoinColumn(name = "date_id", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "hour_id", nullable = false)
    private Hour hour;
    
    private LocalDate date_scheduled;
    
	@Column(name = "scheduled_hour")
	private LocalTime scheduled_hour;
}
