package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.PropertyDescription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyDescriptionRepository extends JpaRepository<PropertyDescription,Long> {
    Optional<PropertyDescription> findByDescriptionIgnoreCase(@NotBlank(message = "Description cannot be empty.") @Size(min = 5, message = "Description must include at least 5 characters.") String description);
}
