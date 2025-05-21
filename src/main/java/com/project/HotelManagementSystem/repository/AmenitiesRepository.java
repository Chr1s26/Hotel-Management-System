package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Amenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenitiesRepository extends JpaRepository<Amenities,Long> {
}
