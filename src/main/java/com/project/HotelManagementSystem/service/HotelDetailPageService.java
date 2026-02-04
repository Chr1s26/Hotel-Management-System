package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.booking.HotelDetailPageDTO;
import com.project.HotelManagementSystem.dto.booking.PhotoDTO;
import com.project.HotelManagementSystem.dto.booking.ReviewDTO;
import com.project.HotelManagementSystem.dto.booking.RoomDetailDTO;
import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelDetailPageService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ReviewRepository reviewRepository;
    private final HotelAttachmentRepository hotelAttachmentRepository;
    private final RoomAttachmentRepository roomAttachmentRepository;
    private final FileService fileService;

    public HotelDetailPageDTO getHotelDetailPage(Long hotelId) {

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

        List<PhotoDTO> photos = new ArrayList<>();

        hotelAttachmentRepository.findByHotelId(hotelId)
                .forEach(att -> photos.add(
                        new PhotoDTO(att.getId(),
                                fileService.getFileNames(FileType.HOTEL_ATTACHMENT, att.getId()),
                                "HOTEL"))
                );

        roomAttachmentRepository.findByRoomId(hotelId)
                .forEach(att -> photos.add(
                        new PhotoDTO(
                                att.getId(),
                                fileService.getFileNames(FileType.ROOM_ATTACHMENT, att.getId()),
                                "ROOM"))
                );

        dto.setPhotos(photos);

        dto.setReviews(
                reviewRepository.findByHotelId(hotelId).stream().map(r -> new ReviewDTO(
                                r.getCustomer().getName(),
                                r.getDescription(),
                                r.getRating()))
                        .toList()
        );

        Set<String> amenities = hotel.getRooms().stream()
                .flatMap(r -> r.getAmenities().stream())
                .map(Amenities::getName)
                .collect(Collectors.toSet());

        dto.setAmenities(amenities);

        List<RoomDetailDTO> roomDTOs = new ArrayList<>();

        for (Room room : hotel.getRooms()) {

            RoomDetailDTO rd = new RoomDetailDTO();
            rd.setRoomId(room.getId());
            rd.setRoomTypeName(room.getRoomType().getName());
            rd.setRoomSize(room.getRoomType().getRoomSize());
            rd.setCapacity(room.getRoomType().getCapacity());
            rd.setPrice(room.getRoomType().getPrice());
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

        dto.setRooms(roomDTOs);
        return dto;
    }
}
