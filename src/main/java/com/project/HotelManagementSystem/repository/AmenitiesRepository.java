package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Amenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmenitiesRepository extends JpaRepository<Amenities,Long>, JpaSpecificationExecutor<Amenities> {
    Optional<Amenities> findByNameAndDescriptionIgnoreCase(String name, String description);
    Optional<Amenities> findByNameAndDescriptionIgnoreCaseAndIdNot(String name, String description, Long id);
}
