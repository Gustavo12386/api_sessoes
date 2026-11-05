package com.example.apisessoes.entities;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
	    @GeneratedValue(strategy = GenerationType.UUID)
	    @Column(
	        name = "especialization_id",
	        columnDefinition = "UUID"
	    )
	    @ColumnDefault("gen_random_uuid()")
	private UUID especializationId;
	private String especializationName;	

	
	@JsonIgnore
	@OneToMany(
	    mappedBy = "especiality",
	    cascade = CascadeType.ALL,
	    orphanRemoval = true
	)
	private List<Date> date = new ArrayList<>();
	
	
}
