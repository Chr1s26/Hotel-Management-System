package com.project.HotelManagementSystem.dto.booking;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class BookingSummaryDTO {
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int totalNights;

    private String hotelName;
    private double hotelRating;
    private int reviewCount;

    private String roomType;
    private int roomSize;
    private int capacity;
    private Set<String> amenities;

    private double originalPrice;
    private double discount;
    private double tax;
    private double finalPrice;
}
