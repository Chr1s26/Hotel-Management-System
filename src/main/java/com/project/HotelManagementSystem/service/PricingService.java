package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.repository.RoomRepository;
import com.project.HotelManagementSystem.dto.booking.PricingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final RoomRepository roomRepository;
    private final PromotionCalculator promotionCalculator;

    public PricingResult calculateHotelPrice(Hotel hotel, LocalDate checkIn, LocalDate checkOut, int guests) {

        List<Room> rooms = roomRepository.findAvailableRoomsByHotelAndGuests(hotel.getId(), guests);

        if (rooms.isEmpty()) {
            return new PricingResult(0, 0, null, false);
        }

        Room cheapestRoom = rooms.stream()
                .min(Comparator.comparingDouble(r -> r.getRoomType().getPrice()))
                .orElseThrow();

//        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
//        double basePrice = cheapestRoom.getRoomType().getPrice() * nights;

        return promotionCalculator.applyPromotion(cheapestRoom.getRoomType().getPrice(), cheapestRoom.getPromotions(), hotel.getPromotions(), checkIn);
    }

}

