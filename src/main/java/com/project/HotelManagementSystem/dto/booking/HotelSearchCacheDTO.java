package com.project.HotelManagementSystem.dto.booking;

import lombok.Data;

import java.util.List;

@Data
public class HotelSearchCacheDTO {

    private Long hotelId;
    private String hotelName;

    private String city;
    private String region;
    private String country;

    private double reviewAvg;
    private int reviewCount;

    private double originalPrice;
    private double discountedPrice;

    private List<String> imageUrls;
}