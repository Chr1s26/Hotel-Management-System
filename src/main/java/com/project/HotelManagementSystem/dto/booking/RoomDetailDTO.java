package com.project.HotelManagementSystem.dto.booking;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class RoomDetailDTO {
    private Long roomId;
    private String roomTypeName;
    private int roomSize;
    private int capacity;
    private double price;
    private Set<String> amenities;
    private List<PhotoDTO> photos;
}
