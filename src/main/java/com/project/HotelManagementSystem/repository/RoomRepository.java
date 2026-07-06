package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.dto.booking.RoomTypeAvailabilityDTO;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.entity.constants.BookingStatus;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long>, JpaSpecificationExecutor<Room> {

    /** Locks the room row (SELECT ... FOR UPDATE) so concurrent checkout finalizations serialize. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Room r WHERE r.id = :id")
    Optional<Room> findLockedById(@org.springframework.data.repository.query.Param("id") Long id);

    /** Count of active (PENDING/CONFIRMED) bookings for this room overlapping the date range. */
    @Query("""
        SELECT COUNT(br) FROM BookingRoom br
        WHERE br.room.id = :roomId
          AND br.booking.bookingStatus IN :statuses
          AND br.booking.checkInDate < :checkOut
          AND :checkIn < br.booking.checkOutDate
    """)
    long countActiveOverlaps(@org.springframework.data.repository.query.Param("roomId") Long roomId,
                             @org.springframework.data.repository.query.Param("checkIn") LocalDate checkIn,
                             @org.springframework.data.repository.query.Param("checkOut") LocalDate checkOut,
                             @org.springframework.data.repository.query.Param("statuses") Collection<BookingStatus> statuses);

    /**
     * Overlap count in the given statuses, EXCLUDING one booking (the one being finalized).
     * At the webhook we pass statuses = {CONFIRMED}: the winner is the first booking to reach
     * CONFIRMED. Two still-PENDING bookings therefore never reject each other.
     */
    @Query("""
        SELECT COUNT(br) FROM BookingRoom br
        WHERE br.room.id = :roomId
          AND br.booking.id <> :excludeBookingId
          AND br.booking.bookingStatus IN :statuses
          AND br.booking.checkInDate < :checkOut
          AND :checkIn < br.booking.checkOutDate
    """)
    long countOverlapsExcludingBooking(@org.springframework.data.repository.query.Param("roomId") Long roomId,
                                       @org.springframework.data.repository.query.Param("checkIn") LocalDate checkIn,
                                       @org.springframework.data.repository.query.Param("checkOut") LocalDate checkOut,
                                       @org.springframework.data.repository.query.Param("statuses") Collection<BookingStatus> statuses,
                                       @org.springframework.data.repository.query.Param("excludeBookingId") Long excludeBookingId);

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
