package com.project.HotelManagementSystem.dto.address;

import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private Long id;
    private double latitude;
    private double longitude;
    private String road;
    private City city;
    private String zipCode;
    private StatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User createdBy;
    private User updatedBy;
}
