package com.example.apisessoes.entities;

import java.time.LocalTime;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
        name = "hour_id",
        columnDefinition = "UUID"
    )
    @ColumnDefault("gen_random_uuid()")
	private UUID hourId;
	
	@Column(name = "current_hour")
    private LocalTime currentHour;
	
	@ManyToOne
	@JoinColumn(name = "date_id", referencedColumnName = "date_id")
	private Date date; 
}
