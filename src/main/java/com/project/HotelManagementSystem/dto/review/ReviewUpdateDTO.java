package com.project.HotelManagementSystem.dto.review;

import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateDTO {
    private Long id;
    private String description;
    private double rating;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reviewDate;
    private User user;
    private Hotel hotel;
}
