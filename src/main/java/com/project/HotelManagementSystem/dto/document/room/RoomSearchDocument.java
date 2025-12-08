package com.project.HotelManagementSystem.dto.document.room;

import com.project.HotelManagementSystem.entity.constants.RoomType;
import lombok.Data;

@Data
public class RoomSearchDocument {
    private String id;
    private Double price;
    private boolean isAvailable;
    private RoomType roomType;
    private int maxCapacity;
}
