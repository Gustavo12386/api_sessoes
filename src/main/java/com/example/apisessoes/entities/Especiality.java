package com.example.apisessoes.entities;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "tb_especialization")
public class Especiality {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private UUID especialization_id;
	private String especializationName;	

	
	@JsonIgnore
	@OneToMany(
	    mappedBy = "especiality",
	    cascade = CascadeType.ALL,
	    orphanRemoval = true
	)
	private List<Date> date = new ArrayList<>();
	
	
	@JsonIgnore
	@OneToMany(
	    mappedBy = "especiality",
	    cascade = CascadeType.ALL,
	    orphanRemoval = true
	)
	private List<Hour> hour = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(
	    mappedBy = "especiality",
	    cascade = CascadeType.ALL,
	    orphanRemoval = true
	)
	private List<Information> informations = new ArrayList<>();
	
}
