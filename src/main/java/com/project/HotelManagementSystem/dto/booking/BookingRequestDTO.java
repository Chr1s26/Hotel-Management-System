package com.project.HotelManagementSystem.dto.booking;

import lombok.Data;

@Data
public class BookingRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String phone;
    private String smokingPreference;
    private String bedPreference;
    private String description;
}
