package com.project.HotelManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int roomNumber;

    @Column
    private double price;

    @Column
    private boolean isAvailable;

    @Column
    private int floor;

    @Column
    private String description;

    @Column
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "amenities_id")
    private Amenities amenities;

    @ManyToOne
    @JoinColumn(name = "roomType_id")
    private RoomType roomType;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
