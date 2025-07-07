package com.project.HotelManagementSystem.dto.propertyDescription;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDescriptionCreateDTO {
    private Long id;

    @NotBlank(message = "Description cannot be empty.")
    @Size(min = 5, message = "Description must include at least 5 characters.")
    private String description;

    @NotNull(message = "Opening Date cannot be empty.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate openingDate;

    @Min(value = 1, message = "Number of room cannot be less than 1.")
    private int numberOfRooms;
}
