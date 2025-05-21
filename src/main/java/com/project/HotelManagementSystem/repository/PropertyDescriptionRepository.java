package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.PropertyDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyDescriptionRepository extends JpaRepository<PropertyDescription,Long> {
}
