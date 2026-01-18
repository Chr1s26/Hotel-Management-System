package com.project.HotelManagementSystem.dto.room;

import com.project.HotelManagementSystem.entity.Hotel;
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
public class RoomUpdateDTO {
    private boolean available;
    @NotBlank(message = "Description cannot be empty.")
    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters.")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,'\\-()]+$", message = "Description can only contain letters, numbers, spaces, and symbols (.,' - ()).")
    private String description;
    private Long roomTypeId;
    @NotNull(message = "Hotel must be selected.")
    private Hotel hotel;
    @NotEmpty(message = "At least one amenity must be selected.")
    private Set<@NotNull(message = "Amenity ID cannot be null.") Long> amenityIds = new HashSet<>();
    private Set<@NotNull(message = "Promotion ID cannot be null.") Long> promotionIds = new HashSet<>();
    private List<MultipartFile> files;
    @NotNull(message = "Media type must be selected")
    private RoomMediaType roomMediaType;
}
