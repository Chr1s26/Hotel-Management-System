package com.project.HotelManagementSystem.dto.review;

import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateDTO {
    private Long id;

    @NotBlank(message = "Description cannot be empty.")
    private String description;

    @DecimalMin(value = "1.0", inclusive = true, message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must be at most 5.0")
    private double rating;

    @NotNull(message = "Review Date cannot be empty.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reviewDate;

    @NotNull(message = "User cannot be empty.")
    private User user;

    @NotNull(message = "Hotel cannot be emoty.")
    private Hotel hotel;
}
