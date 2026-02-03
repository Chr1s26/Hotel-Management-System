package com.project.HotelManagementSystem.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelSearchDocument {

    private Long hotelId;

    private String hotelName;

    private String city;
    private String region;
    private String country;

    private String hotelType;

    private double minRoomPrice;
    private double maxRoomPrice;

    private double reviewAvg;
    private int reviewCount;

    private Set<String> roomTypeNames;
    private Set<String> amenities;

    private boolean hasPromotion;
}
