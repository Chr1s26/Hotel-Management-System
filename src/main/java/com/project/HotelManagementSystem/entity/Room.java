package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.converter.RoomTypeConverter;
import com.project.HotelManagementSystem.entity.constants.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room extends MasterData {

    @Column(nullable = false)
    private double price;

    @Column(name = "is_available", nullable = false)
    private boolean available;

    @Column(nullable = true)
    private String description;

    @Column(name = "room_type", nullable = false)
    @Convert(converter = RoomTypeConverter.class)
    private RoomType roomType;

    @Column(nullable = false)
    private int maxCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToMany(mappedBy = "rooms")
    private Set<Booking> bookings = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "room_amenities",
    joinColumns = @JoinColumn(name = "room_id"),
    inverseJoinColumns = @JoinColumn(name = "amenities_id)"))
    private Set<Amenities> amenities = new HashSet<>();

    @ManyToMany(fetch =  FetchType.LAZY)
    @JoinTable(name = "room_promotion",
    joinColumns = @JoinColumn(name = "room_id"),
    inverseJoinColumns = @JoinColumn(name = "promotion_id"))
    private Set<Promotion> promotions = new HashSet<>();

    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL)
    private List<RoomAttachment> roomAttachments = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<CartItem> cartItems = new ArrayList<>();

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
