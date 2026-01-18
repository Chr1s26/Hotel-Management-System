package com.project.HotelManagementSystem.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomTypeAvailabilityDTO {
    private Long roomTypeId;
    private String name;
    private int roomSize;
    private int capacity;
    private double price;
    private long availableRooms;
}
