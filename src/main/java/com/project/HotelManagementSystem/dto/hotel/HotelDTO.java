package com.project.HotelManagementSystem.dto.hotel;

import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.entity.PropertyDescription;
import com.project.HotelManagementSystem.entity.constants.HotelType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
