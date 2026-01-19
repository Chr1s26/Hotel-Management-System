package com.project.HotelManagementSystem.dto.booking;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingCreateDTO {
    private Long hotelId;
    private Long roomTypeId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String description;
    private int numberOfRooms;
    private int numberOfGuests;
}
