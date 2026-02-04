package com.project.HotelManagementSystem.dto.booking;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class HotelDetailPageDTO {
    private Long hotelId;
    private String hotelName;
    private String address;
    private double rating;
    private int reviewCount;
    private String description;
    private List<PhotoDTO> photos;
    private List<ReviewDTO> reviews;
    private Set<String> amenities;
    private List<RoomDetailDTO> rooms;
}
