package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Long>, JpaSpecificationExecutor<Promotion> {
    Optional<Promotion> findByCode(String code);
    Optional<Promotion> findByCodeAndIdNot(String code, Long id);
}
