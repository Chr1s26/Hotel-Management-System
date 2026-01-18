package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.dto.booking.RoomTypeAvailabilityDTO;
import com.project.HotelManagementSystem.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long>, JpaSpecificationExecutor<Room> {
    @Query("""
    SELECT new com.project.dto.RoomTypeAvailabilityDTO(
    rt.id,
    rt.name,
    rt.roomSize,
    rt.capacity,
    rt.price,
    COUNT(r)
    )
    FROM Room r
    JOIN r.roomType rt
    WHERE r.hotel.id = :hotelId
    AND rt.capacity >= :guests
    AND r.id NOT IN (
        SELECT br.id FROM Booking b
        JOIN b.rooms br
        WHERE b.bookingStatus <> 'CANCELLED'
        AND b.checkInDate < :checkOut
        AND :checkIn < b.checkOutDate
    )
    GROUP BY rt.id, rt.name, rt.roomSize, rt.capacity, rt.price
    """)
    List<RoomTypeAvailabilityDTO> findRoomTypeAvailability(Long hotelId, LocalDate checkIn, LocalDate checkOut, int guests);
}
