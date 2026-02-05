package com.project.HotelManagementSystem.dto.booking;

import lombok.Data;

@Data
public class ReviewCreateDTO {
    private Long hotelId;
    private String comment;
    private double rating;
}
