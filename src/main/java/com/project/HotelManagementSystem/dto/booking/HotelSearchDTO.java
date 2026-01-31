package com.project.HotelManagementSystem.dto.booking;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class HotelSearchDTO {
    private String keyword;
    private Long hotelId;
    private Long countryId;
    private Long regionId;
    private Long cityId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOut;
    private int numberOfGuests;
}
