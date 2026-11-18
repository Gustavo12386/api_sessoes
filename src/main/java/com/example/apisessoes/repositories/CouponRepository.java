package com.example.apisessoes.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.apisessoes.entities.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
	Optional<Coupon> findByCouponCode(String couponCode);

    boolean existsByCouponCode(String couponCode);
}
