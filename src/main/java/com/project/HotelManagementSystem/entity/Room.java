package com.project.HotelManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.HotelManagementSystem.converter.RoomTypeConverter;
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

    @Column(name = "is_available", nullable = false)
    private boolean available;

    @Column(nullable = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    private Hotel hotel;

    @ManyToMany(mappedBy = "rooms")
    @JsonIgnore
    private Set<Booking> bookings = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "room_amenities",
    joinColumns = @JoinColumn(name = "room_id"),
    inverseJoinColumns = @JoinColumn(name = "amenities_id)"))
    @JsonIgnore
    private Set<Amenities> amenities = new HashSet<>();

    @ManyToMany(fetch =  FetchType.LAZY)
    @JoinTable(name = "room_promotion",
    joinColumns = @JoinColumn(name = "room_id"),
    inverseJoinColumns = @JoinColumn(name = "promotion_id"))
    @JsonIgnore
    private Set<Promotion> promotions = new HashSet<>();

    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RoomAttachment> roomAttachments = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<CartItem> cartItems = new ArrayList<>();

    public boolean isAvailable() { return available;}

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
