package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByHotelId(Long hotelId);
    List<Review> findByHotelIdOrderByCreatedAtDesc(Long hotelId);
    Optional<Review> findByIdAndCustomerId(Long id, Long customerId);
}
