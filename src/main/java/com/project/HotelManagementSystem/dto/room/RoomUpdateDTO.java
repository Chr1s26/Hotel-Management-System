package com.project.HotelManagementSystem.dto.room;

import com.project.HotelManagementSystem.entity.Hotel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomUpdateDTO {
    private Long id;

    @DecimalMin(value = "1.0", inclusive = true, message = "Price cannot be empty.")
    private double price;
    private boolean available;
    @NotBlank(message = "Description cannot be empty.")
    private String description;
    private String roomType;
    @Min(value = 1, message = "Max capacity cannot be less than 1.")
    private int maxCapacity;
    private Hotel hotel;
    private Set<Long> amenityIds = new HashSet<>();
    private Set<Long> promotionIds = new HashSet<>();
}
