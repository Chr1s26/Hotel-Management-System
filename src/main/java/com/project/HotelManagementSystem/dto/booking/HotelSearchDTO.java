package com.project.HotelManagementSystem.dto.booking;

import com.project.HotelManagementSystem.entity.constants.HotelType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
    private List<HotelType> hotelTypes;
    private Double minPrice;
    private Double maxPrice;
    private Double minRating;
    private Set<String> roomTypeNames;
    private Set<String> amenities;
    private boolean promotion;
}
