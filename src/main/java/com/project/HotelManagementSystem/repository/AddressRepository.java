package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByLatitudeAndLongitude(Double latitude, Double longitude);
    Optional<Address> findByLatitudeAndLongitudeAndIdNot(Double latitude, Double longitude, Long id);
}
