package com.project.HotelManagementSystem.dto.room;

import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private Long id;
    private boolean available;
    private String description;
    private Integer quantity;
    private String roomTypeName;
    private double price;
    private int roomSize;
    private Hotel hotel;
    private Set<Amenities> amenities = new HashSet<>();
    private Set<Promotion> promotions = new HashSet<>();
    private StatusType status;
    private User createdBy;
    private User updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
