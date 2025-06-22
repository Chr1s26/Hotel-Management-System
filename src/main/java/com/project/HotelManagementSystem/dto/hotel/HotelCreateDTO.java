package com.project.HotelManagementSystem.dto.hotel;

import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.PropertyDescription;
import com.project.HotelManagementSystem.entity.constants.HotelType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelCreateDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String description;
    private double rating;
    private HotelType hotelType;
    private Address address;
    private PropertyDescription propertyDescription;
    private Set<Long> promotionIds = new HashSet<>();
    private Set<Long> policyIds = new HashSet<>();
}
