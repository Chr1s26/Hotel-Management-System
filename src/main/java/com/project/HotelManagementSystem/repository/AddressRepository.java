package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Address;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> , JpaSpecificationExecutor<Address> {
    Optional<Address> findByLatitudeAndLongitude(Double latitude, Double longitude);
    Optional<Address> findByLatitudeAndLongitudeAndIdNot(Double latitude, Double longitude, Long id);
    List<Address> findByHotelIsNull();
    @Query("""
    select distinct a from Address a
    left join a.hotel h
    where h is null or h.id = :hotelId
""")
    List<Address> findAvailableForUpdate(@Param("hotelId") Long hotelId);
}
