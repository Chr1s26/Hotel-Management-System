package com.project.HotelManagementSystem.dto.hotel;

import com.project.HotelManagementSystem.entity.*;
import com.project.HotelManagementSystem.entity.constants.HotelType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HotelDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String description;
    private double rating;
    private HotelType hotelType;
    private Address address;
    private PropertyDescription propertyDescription;
    private Set<Promotion> promotions = new HashSet<>();
    private Set<Policy>  policies = new HashSet<>();
    private String profileUrl;
    private StatusType status;
    private User createdBy;
    private User updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
