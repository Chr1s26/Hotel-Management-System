package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) = LOWER(:name) AND h.address.latitude = :latitude AND h.address.longitude = :longitude")
    Optional<Hotel> findHotelByNameAndCoordinates(String name, double latitude, double longitude);

    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) = LOWER(:name) AND h.address.latitude = :latitude AND h.address.longitude = :longitude AND h.id <> :id")
    Optional<Hotel> findHotelByNameAndCoordinatesAndIdNot(String name, double latitude, double longitude, Long id);

    Optional<Hotel> findHotelByAddress(Address address);
    Optional<Hotel> findHotelByAddressAndIdNot(Address address,Long id);

    @Query("""
        SELECT DISTINCT h FROM Hotel h
        JOIN h.address a
        JOIN a.city c
        JOIN c.region r
        JOIN r.country co
        WHERE (
            :keyword IS NULL OR :keyword = '' OR
            LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(co.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    List<Hotel> searchByKeyword(String keyword);
}
