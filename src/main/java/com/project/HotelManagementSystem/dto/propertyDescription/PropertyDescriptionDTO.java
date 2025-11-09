package com.project.HotelManagementSystem.dto.propertyDescription;

import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDescriptionDTO {
    private Long id;
    private String description;
    private LocalDate openingDate;
    private LocalDate renovationDate;
    private int numberOfRooms;
    private StatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User createdBy;
    private User updatedBy;
}
