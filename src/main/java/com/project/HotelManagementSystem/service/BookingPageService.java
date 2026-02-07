package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.booking.BookingSummaryDTO;
import com.project.HotelManagementSystem.dto.booking.PhotoDTO;
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

@Service
@RequiredArgsConstructor
public class BookingPageService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final HotelAttachmentRepository hotelAttachmentRepository;
    private final FileService fileService;

    public BookingSummaryDTO prepareBooking(Long hotelId, Long roomId, LocalDate checkIn, LocalDate checkOut) {

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();

        int nights = (int) ChronoUnit.DAYS.between(checkIn, checkOut);
        double basePrice = room.getRoomType().getPrice();
        double originalTotal = basePrice * nights;

        double discount = originalTotal * 0.4;
        double tax = (originalTotal - discount) * 0.1;

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

        dto.setOriginalPrice(originalTotal);
        dto.setDiscount(discount);
        dto.setTax(tax);
        dto.setFinalPrice(originalTotal - discount + tax);

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
}
