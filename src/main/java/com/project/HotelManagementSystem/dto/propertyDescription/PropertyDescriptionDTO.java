package com.project.HotelManagementSystem.dto.propertyDescription;

import com.project.HotelManagementSystem.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDescriptionDTO {
    private Long id;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate openingDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate renovationDate;
    private int numberOfRooms;
}
