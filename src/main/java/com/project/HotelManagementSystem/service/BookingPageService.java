package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.booking.BookingSummaryDTO;
import com.project.HotelManagementSystem.dto.booking.PhotoDTO;
import com.project.HotelManagementSystem.dto.booking.PricingResult;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.repository.HotelAttachmentRepository;
import com.project.HotelManagementSystem.repository.HotelRepository;
import com.project.HotelManagementSystem.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingPageService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final HotelAttachmentRepository hotelAttachmentRepository;
    private final FileService fileService;
    private final PromotionCalculator promotionCalculator;

    public BookingSummaryDTO prepareBooking(Long hotelId, Long roomId, LocalDate checkIn, LocalDate checkOut) {

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();

        int nights = (int) ChronoUnit.DAYS.between(checkIn, checkOut);
        double pricePerNight = room.getRoomType().getPrice();

        PricingResult pricing = promotionCalculator.applyPromotion(pricePerNight,room.getPromotions(),hotel.getPromotions(),checkIn);

        double discountedPerNight = pricing.getDiscountedPrice();
        double originalTotal = pricePerNight * nights;
        double discountedTotal = discountedPerNight * nights;
        double discountAmount = round2(originalTotal - discountedTotal);
        double tax = round2(discountedTotal * 0.10);
        double finalTotal = round2(discountedTotal + tax);

        BookingSummaryDTO dto = new BookingSummaryDTO();
        dto.setCheckIn(checkIn);
        dto.setCheckOut(checkOut);
        dto.setTotalNights(nights);

        dto.setHotelName(hotel.getName());
        dto.setHotelRating(hotel.getRating());
        dto.setReviewCount(hotel.getReviews().size());

        dto.setRoomType(room.getRoomType().getName());
        dto.setRoomSize(room.getRoomType().getRoomSize());
        dto.setCapacity(room.getRoomType().getCapacity());

        dto.setAmenities(
                room.getAmenities()
                        .stream()
                        .map(a -> a.getName())
                        .collect(Collectors.toSet())
        );

        dto.setOriginalPrice(originalTotal);
        dto.setDiscount(discountAmount);
        dto.setTax(tax);
        dto.setFinalPrice(finalTotal);

        List<PhotoDTO> photos = new ArrayList<>();

        hotelAttachmentRepository.findByHotelId(hotelId)
                .forEach(att -> photos.add(
                        new PhotoDTO(att.getId(),
                                fileService.getFileNames(FileType.HOTEL_ATTACHMENT, att.getId()),
                                "HOTEL"))
                );
        dto.setPhotos(photos);

        return dto;
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
