package com.project.HotelManagementSystem.dto.booking;

import lombok.Data;

@Data
public class HotelSearchResultDTO {
    private Long hotelId;
    private String hotelName;
    private double rating;
    private String description;

    private String city;
    private String region;
    private String country;
    private String address;

    private String imageUrl;
    private double totalPrice;
    private int nights;
}
