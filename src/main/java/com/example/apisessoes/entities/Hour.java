package com.example.apisessoes.entities;

import java.time.LocalDate;


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
@Table(name = "tb_hours")
public class Hour {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private UUID hour_id;
	
	private LocalDate current_hour;	
	
	@ManyToOne
	@JoinColumn(name = "date_id", referencedColumnName = "date_id")
	private Date date; 
}
