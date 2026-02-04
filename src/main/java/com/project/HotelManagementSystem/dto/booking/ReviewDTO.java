package com.project.HotelManagementSystem.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewDTO {
    private String customerName;
    private String comment;
    private double rating;
}
