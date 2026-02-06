package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.booking.*;
import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelDetailPageService {
    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;
    private final HotelAttachmentRepository hotelAttachmentRepository;
    private final RoomAttachmentRepository roomAttachmentRepository;
    private final FileService fileService;
    private final PromotionCalculator promotionCalculator;

    public HotelDetailPageDTO getHotelDetailPage(Long hotelId,Long currentCustomerId) {

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow();

        HotelDetailPageDTO dto = new HotelDetailPageDTO();
        dto.setHotelId(hotel.getId());
        dto.setHotelName(hotel.getName());
        dto.setDescription(hotel.getDescription());
        dto.setRating(hotel.getRating());
        dto.setReviewCount(hotel.getReviews().size());
        dto.setAddress(hotel.getAddress().getCity().getName() + ", " +
                        hotel.getAddress().getCity().getRegion().getName() + ", " +
                        hotel.getAddress().getCity().getRegion().getCountry().getName());

        if (hotel.getPropertyDescription() != null) {
            dto.setPropertyDescription(hotel.getPropertyDescription().getDescription());
            dto.setNumberOfRooms(hotel.getPropertyDescription().getNumberOfRooms());
            dto.setOpeningDate(hotel.getPropertyDescription().getOpeningDate());
            dto.setRenovationDate(hotel.getPropertyDescription().getRenovationDate());
        }

        Hotel hotel_policy = hotelRepository.findByIdWithPolicies(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));


        dto.setPolicies(hotel_policy.getPolicies().stream()
                        .map(p -> new PolicyDTO(
                                p.getTitle(),
                                p.getDescription(),
                                p.getApplicableTo()
                        ))
                        .toList()
        );


        List<PhotoDTO> photos = new ArrayList<>();

        hotelAttachmentRepository.findByHotelId(hotelId)
                .forEach(att -> photos.add(
                        new PhotoDTO(att.getId(),
                                fileService.getFileNames(FileType.HOTEL_ATTACHMENT, att.getId()),
                                "HOTEL"))
                );

        dto.setReviews(
                reviewRepository.findByHotelId(hotelId)
                        .stream()
                        .map(r -> new ReviewDTO(
                                r.getId(),
                                r.getCustomer().getUser().getName(),
                                r.getDescription(),
                                r.getRating(),
                                r.getLikes().size(),
                                currentCustomerId != null &&
                                        r.getLikes().stream()
                                                .anyMatch(l ->
                                                        l.getCustomer().getId().equals(currentCustomerId)
                                                ),
                                currentCustomerId != null &&
                                        r.getCustomer().getId().equals(currentCustomerId)
                        ))
                        .toList()
        );

        Set<String> amenities = hotel.getRooms().stream()
                .flatMap(r -> r.getAmenities().stream())
                .map(Amenities::getName)
                .collect(Collectors.toSet());

        dto.setAmenities(amenities);

        List<RoomDetailDTO> roomDTOs = new ArrayList<>();

        for (Room room : hotel.getRooms()) {

            roomAttachmentRepository.findByRoomId(room.getId())
                    .forEach(att -> photos.add(
                            new PhotoDTO(
                                    att.getId(),
                                    fileService.getFileNames(FileType.ROOM_ATTACHMENT, att.getId()),
                                    "ROOM"))
                    );

            RoomDetailDTO rd = new RoomDetailDTO();
            rd.setRoomId(room.getId());
            rd.setRoomTypeName(room.getRoomType().getName());
            rd.setRoomSize(room.getRoomType().getRoomSize());
            rd.setCapacity(room.getRoomType().getCapacity());
            double basePrice = room.getRoomType().getPrice();

            PricingResult pricing = promotionCalculator.applyPromotion(
                    basePrice,
                    room.getPromotions(),
                    hotel.getPromotions(),
                    LocalDate.now()
            );

            rd.setOriginalPrice(pricing.getOriginalPrice());
            rd.setFinalPrice(pricing.getDiscountedPrice());
            rd.setPromotionApplied(pricing.isHasPromotion());
            rd.setPromotionLabel(pricing.getDiscountLabel());
            rd.setAmenities(
                    room.getAmenities().stream()
                            .map(Amenities::getName)
                            .collect(Collectors.toSet())
            );
            rd.setPhotos(
                    roomAttachmentRepository.findByRoomId(room.getId())
                            .stream()
                            .map(att -> new PhotoDTO(
                                    att.getId(),
                                    fileService.getFileNames(FileType.ROOM_ATTACHMENT, att.getId()),
                                    "ROOM"
                            )).toList()
            );
            roomDTOs.add(rd);
        }

        dto.setPhotos(photos);
        dto.setRooms(roomDTOs);
        return dto;
    }
}
