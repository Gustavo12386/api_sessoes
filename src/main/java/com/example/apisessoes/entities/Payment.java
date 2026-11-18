package com.example.apisessoes.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "tb_payments")
public class Payment {
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
        name = "id",
        columnDefinition = "UUID"
    )
    @ColumnDefault("gen_random_uuid()")
	private UUID id;
   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "information_id",
        nullable = false
    )
    private Information information;

    private String preferenceId;
    
    @Column(name = "mercado_pago_payment_id")
    private String mercadoPagoPaymentId;

    private String externalReference;

    private String mercadoPagoOrderId;

    private BigDecimal originalAmount;

    private BigDecimal discountAmount;

    private BigDecimal finalAmount;

    private String couponCode;

    private String paymentMethod;

    private String status;

    private LocalDateTime createdAt;
}
