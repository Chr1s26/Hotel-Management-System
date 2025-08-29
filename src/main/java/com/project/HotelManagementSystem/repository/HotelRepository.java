package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) = LOWER(:name) AND h.address.latitude = :latitude AND h.address.longitude = :longitude")
    Optional<Hotel> findHotelByNameAndCoordinates(String name, double latitude, double longitude);

    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) = LOWER(:name) AND h.address.latitude = :latitude AND h.address.longitude = :longitude AND h.id <> :id")
    Optional<Hotel> findHotelByNameAndCoordinatesAndIdNot(String name, double latitude, double longitude, Long id);

}
