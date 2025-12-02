package com.project.HotelManagementSystem.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomSimpleDTO {
    private Long id;
    private double price;
    private boolean available;
    private String description;
    private String roomType;
    private Integer quantity;
    private int maxCapacity;
}
