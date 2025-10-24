package com.project.HotelManagementSystem.dto.room;

import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.constants.HotelMediaType;
import com.project.HotelManagementSystem.entity.constants.RoomMediaType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateDTO {
    private Long id;

    @DecimalMin(value = "1.0", inclusive = true, message = "Price cannot be empty.")
    private double price;
    private boolean available;
    @NotBlank(message = "Description cannot be empty.")
    private String description;
    private String roomType;
    @Min(value = 1, message = "Max Capacity cannont be less than 1.")
    private int maxCapacity;
    private Hotel hotel;
    private Set<Long> amenityIds = new HashSet<>();
    private Set<Long> promotionIds = new HashSet<>();
    private List<MultipartFile> files;
    @NotNull(message = "Media type must be selected")
    private RoomMediaType roomMediaType;
}
