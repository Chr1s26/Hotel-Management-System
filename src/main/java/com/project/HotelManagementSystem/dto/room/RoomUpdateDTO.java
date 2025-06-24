package com.project.HotelManagementSystem.dto.room;

import com.project.HotelManagementSystem.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomUpdateDTO {
    private Long id;
    private double price;
    private boolean available;
    private String description;
    private String roomType;
    private int maxCapacity;
    private Hotel hotel;
    private Set<Long> amenityIds = new HashSet<>();
    private Set<Long> promotionIds = new HashSet<>();
}
