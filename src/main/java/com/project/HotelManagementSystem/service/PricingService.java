package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final RoomRepository roomRepository;

    public double calculateTotalPrice(Long hotelId, LocalDate checkIn, LocalDate checkOut) {

        if (checkIn == null || checkOut == null) return 0;
        if (!checkOut.isAfter(checkIn)) return 0;

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) return 0;

        List<Room> rooms = roomRepository.findAvailableRoomsCheapestFirst(hotelId);
        if (rooms.isEmpty()) return 0;

        return rooms.get(0).getRoomType().getPrice() * nights;
    }

}

