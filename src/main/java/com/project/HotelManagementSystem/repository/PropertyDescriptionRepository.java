package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.PropertyDescription;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyDescriptionRepository extends JpaRepository<PropertyDescription,Long>, JpaSpecificationExecutor<PropertyDescription> {
    Optional<PropertyDescription> findByDescriptionIgnoreCase(@NotBlank(message = "Description cannot be empty.") @Size(min = 5, message = "Description must include at least 5 characters.") String description);
    List<PropertyDescription> findByHotelIsNull();
    @Query("""
        select pd from PropertyDescription pd
        where pd.hotel is null or pd.hotel.id = :hotelId
    """)
    List<PropertyDescription> findAvailableForUpdate(@Param("hotelId") Long hotelId);

}
