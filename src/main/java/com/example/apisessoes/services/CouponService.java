package com.example.apisessoes.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apisessoes.dto.patient.CouponResponse;
import com.example.apisessoes.entities.Coupon;
import com.example.apisessoes.repositories.CouponRepository;

@Service
public class CouponService {
	@Autowired
	private CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public CouponResponse validateCoupon(String couponCode) {

        if (couponCode == null || couponCode.isBlank()) {
            throw new IllegalArgumentException(
                "Informe um código de cupom."
            );
        }

        String normalizedCode = couponCode.trim().toUpperCase(Locale.ROOT);

        Coupon coupon = couponRepository
                .findByCouponCode(normalizedCode)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Cupom não encontrado."
                ));

        LocalDateTime now = LocalDateTime.now(
        	    ZoneId.of("America/Sao_Paulo")
        	);

        if (!Boolean.TRUE.equals(coupon.getActive())) {
            throw new IllegalArgumentException("Cupom inativo.");
        }

        if (coupon.getValidFrom() != null &&
        	    coupon.getValidFrom().isAfter(now)) {

        	    throw new IllegalArgumentException(
        	        "Cupom ainda não está válido."
        	    );
        	}

        if (coupon.getValidUntil() != null &&
                now.isAfter(coupon.getValidUntil())) {
            throw new IllegalArgumentException("Cupom expirado.");
        }

        if (coupon.getMaxUses() != null &&
                coupon.getUsesCount() >= coupon.getMaxUses()) {
            throw new IllegalArgumentException(
                "Limite de utilização do cupom atingido."
            );
        }

        return new CouponResponse(
            coupon.getCouponId(),
            coupon.getCouponCode(),
            coupon.getDiscountPercentage(),
            coupon.getValidUntil()
        );
    }

    public BigDecimal calculateDiscount(
            BigDecimal originalAmount,
            BigDecimal percentage) {

        if (originalAmount == null ||
                originalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Valor original inválido."
            );
        }

        if (percentage == null ||
                percentage.compareTo(BigDecimal.ZERO) < 0 ||
                percentage.compareTo(new BigDecimal("100")) > 0) {

            throw new IllegalArgumentException(
                "Percentual de desconto inválido."
            );
        }

        return originalAmount
                .multiply(percentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
}
