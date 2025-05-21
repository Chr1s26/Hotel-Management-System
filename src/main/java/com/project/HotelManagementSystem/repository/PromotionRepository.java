package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Payment,Long> {
}
