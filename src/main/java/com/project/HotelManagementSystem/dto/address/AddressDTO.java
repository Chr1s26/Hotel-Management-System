package com.project.HotelManagementSystem.dto.address;

import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.entity.Hotel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
