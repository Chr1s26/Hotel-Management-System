package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.dto.booking.RoomTypeAvailabilityDTO;
import com.project.HotelManagementSystem.entity.Room;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long>, JpaSpecificationExecutor<Room> {
    @Query("""
SELECT new com.project.HotelManagementSystem.dto.booking.RoomTypeAvailabilityDTO(
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
    SELECT br.room.id
    FROM Booking b
    JOIN b.bookingRooms br
    WHERE b.bookingStatus <> 0
    AND b.checkInDate < :checkOut
    AND :checkIn < b.checkOutDate
)
GROUP BY rt.id, rt.name, rt.roomSize, rt.capacity, rt.price
""")
    List<RoomTypeAvailabilityDTO> findRoomTypeAvailability(Long hotelId, LocalDate checkIn, LocalDate checkOut, int guests);

//    @Query("""
//    SELECT r FROM Room r
//    WHERE r.hotel.id = :hotelId
//      AND r.roomType.id = :roomTypeId
//      AND r.id NOT IN (
//          SELECT br.id FROM Booking b
//          JOIN b.rooms br
//          WHERE b.bookingStatus <> 0
//            AND b.checkInDate < :checkOut
//            AND :checkIn < b.checkOutDate
//      )
//    """)
//    List<Room> findAvailableRooms(Long hotelId, Long roomTypeId, LocalDate checkIn, LocalDate checkOut);

//    @Query("""
//        SELECT r FROM Room r
//        JOIN r.roomType rt
//        WHERE r.hotel.id = :hotelId
//          AND r.available = true
//        ORDER BY rt.price ASC
//    """)
//    List<Room> findAvailableRoomsCheapestFirst(Long hotelId);

    @Query("""
        SELECT DISTINCT r
        FROM Room r
        JOIN r.roomType rt
        WHERE r.hotel.id = :hotelId
          AND r.available = true
          AND rt.capacity >= :guests
    """)
    List<Room> findAvailableRoomsByHotelAndGuests(@Param("hotelId") Long hotelId, @Param("guests") int guests);
}
