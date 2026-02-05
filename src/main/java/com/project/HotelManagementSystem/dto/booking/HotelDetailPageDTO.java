package com.project.HotelManagementSystem.dto.booking;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class HotelDetailPageDTO {
    private Long hotelId;
    private String hotelName;
    private String description;
    private double rating;
    private String address;
    private int reviewCount;

    private String propertyDescription;
    private Integer numberOfRooms;
    private LocalDate openingDate;
    private LocalDate renovationDate;

    private List<PolicyDTO> policies;
    private List<PhotoDTO> photos;
    private List<ReviewDTO> reviews;
    private Set<String> amenities;
    private List<RoomDetailDTO> rooms;
}
