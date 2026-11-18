package com.example.apisessoes.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;

import com.example.apisessoes.entities.Coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
@Table(
    name = "tb_coupons"    
)
public class Coupon {
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
        name = "couponId",
        columnDefinition = "UUID"
    )
    @ColumnDefault("gen_random_uuid()")
    private UUID couponId;

    @Column(name = "coupon_code", nullable = false, unique = true, length = 50)
    private String couponCode;

    @Column(name = "discount_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    @Column(name = "max_uses")
    private Integer maxUses;

    @Column(name = "uses_count", nullable = false)
    private Integer usesCount;

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = true;
        }

        if (usesCount == null) {
            usesCount = 0;
        }
    }
}
