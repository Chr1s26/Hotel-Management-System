package com.project.HotelManagementSystem.dto.booking;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HotelSearchDTO {
    private String keyword;
    private Long countryId;
    private Long regionId;
    private Long cityId;
    private LocalDate checkIn;
    private LocalDate checkout;
    private int numberOfGuests;
}
