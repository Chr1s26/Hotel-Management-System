package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.converter.RoomTypeConverter;
import com.project.HotelManagementSystem.entity.constants.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double price;

    @Column
    private boolean isAvailable;

    @Column
    private int floor;

    @Column
    private String description;

    @Column(name = "room_type")
    @Convert(converter = RoomTypeConverter.class)
    private RoomType roomType;

    @Column
    private int maxCapacity;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToMany
    @JoinTable(name = "room_amenities",
    joinColumns = @JoinColumn(name = "room_id"),
    inverseJoinColumns = @JoinColumn(name = "amenities_id)"))
    private Set<Amenities> amenities = new HashSet<>();
}
