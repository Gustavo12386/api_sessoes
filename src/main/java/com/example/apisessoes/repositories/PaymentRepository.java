package com.example.apisessoes.repositories;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.apisessoes.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID>{
	Optional<Payment> findByExternalReference(String externalReference);
    Optional<Payment> findByMercadoPagoPaymentId(String mercadoPagoPaymentId);
    
}
