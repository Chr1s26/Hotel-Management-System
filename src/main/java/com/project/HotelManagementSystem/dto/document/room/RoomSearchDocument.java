package com.project.HotelManagementSystem.dto.document.room;

import com.project.HotelManagementSystem.entity.RoomType;
import lombok.Data;

@Data
public class RoomSearchDocument {
    private String id;
    private Double price;
    private boolean isAvailable;
    private String roomType;
    private int maxCapacity;
}
