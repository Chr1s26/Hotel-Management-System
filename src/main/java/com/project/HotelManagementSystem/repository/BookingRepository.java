package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    Optional<Booking> findByPaymentIntentId(String paymentIntentId);
}
