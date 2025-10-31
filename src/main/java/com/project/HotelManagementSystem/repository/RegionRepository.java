package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Region;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region,Long> {
    Optional<Region> findByNameIgnoreCase(String name);
    Page<Region> findAll(Specification<Region> spec, Pageable pageable);
    Optional<Region> findByNameIgnoreCaseAndIdNot(String name, Long id);
}
