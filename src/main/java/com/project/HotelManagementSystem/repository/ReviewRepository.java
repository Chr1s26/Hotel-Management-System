package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByHotelId(Long hotelId);
}
