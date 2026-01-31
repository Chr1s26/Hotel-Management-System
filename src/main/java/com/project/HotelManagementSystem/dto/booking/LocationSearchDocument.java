package com.project.HotelManagementSystem.dto.booking;

import lombok.Data;

@Data
public class LocationSearchDocument {
    private String id;
    private String name;
    private String type;
    private Long hotelId;
    private Long cityId;
    private Long regionId;
    private Long countryId;
    private String cityName;
    private String countryName;
    private String regionName;
}
