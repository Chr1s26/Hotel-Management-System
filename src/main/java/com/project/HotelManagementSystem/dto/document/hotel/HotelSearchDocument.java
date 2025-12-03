package com.project.HotelManagementSystem.dto.document.hotel;

import lombok.Data;

@Data
public class HotelSearchDocument {
    private String id;
    private String name;
    private String city;
    private String description;
    private Double rating;
}
