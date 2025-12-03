package com.project.HotelManagementSystem.entity.document;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hotels")
public class Hotel {
    @Id
    private String id;
    private String name;
    private String city;
    private String region;
    private String description;
    private Double rating;
}
