package com.example.apisessoes.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_information_patient")
public class Information {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private UUID id;
	@ManyToOne
	@JoinColumn(name = "especialization_id", referencedColumnName = "especialization_id")
	private Especiality especiality;
	private String name;	
	private String phone;	 
	private String email;
	private String password;
	private LocalDate date_birth;
	private LocalDate date_scheduled;
	private String hour;	
	
}
