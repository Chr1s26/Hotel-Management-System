package com.project.HotelManagementSystem.dto.propertyDescription;

import com.project.HotelManagementSystem.entity.Hotel;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDescriptionUpdateDTO {
    private Long id;
    @NotBlank(message = "Property description cannot be empty.")
    @Size(min = 5, max = 500, message = "Property description must be between 5 and 500 characters.")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,'\\-()]+$", message = "Description can only contain letters, numbers, spaces, and symbols (.,' - ()).")
    private String description;
    @NotNull(message = "Opening Date cannot be empty.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate openingDate;
    @NotNull(message = "Revnovation Date cannot be empty.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate renovationDate;
    @NotNull(message = "Rooms cannot be null")
    @Min(value = 1, message = "Number of room cannot be less than 1.")
    private Integer numberOfRooms;
}
