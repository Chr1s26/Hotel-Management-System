package com.project.HotelManagementSystem.dto.room;

import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.Promotion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private Long id;
    private double price;
    private boolean available;
    private String description;
    private String roomType;
    private int maxCapacity;
    private Hotel hotel;
    private Set<Amenities> amenities = new HashSet<>();
    private Set<Promotion> promotions = new HashSet<>();
}
