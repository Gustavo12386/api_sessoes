package com.example.apisessoes.entities;

import java.time.LocalDate;
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
@Table(name = "tb_dates")
public class Date {
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
        name = "date_id",
        columnDefinition = "UUID"
    )
    @ColumnDefault("gen_random_uuid()")
	private UUID dateId;
	@Column(name = "date_value")
	private LocalDate currentDate;	
	
	@ManyToOne
	@JoinColumn(name = "especialization_id", referencedColumnName = "especialization_id")
	private Especiality especiality;
	
	@JsonIgnore
	@OneToMany(mappedBy = "date", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Hour> hour = new ArrayList<>();		
	
	
}
