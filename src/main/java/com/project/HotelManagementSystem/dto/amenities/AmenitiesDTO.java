package com.project.HotelManagementSystem.dto.amenities;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmenitiesDTO {
    private Long id;
    private String name;
    private String description;
    private StatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User createdBy;
    private User updatedBy;
}
