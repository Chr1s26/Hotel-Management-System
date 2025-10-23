package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.converter.HotelTypeConverter;
import com.project.HotelManagementSystem.entity.constants.HotelType;
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
@Table(name = "hotels")
public class Hotel extends  MasterData {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private double rating;

    @Column(name = "hotel_type")
    @Convert(converter = HotelTypeConverter.class)
    private HotelType hotelType;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne
    @JoinColumn(name = "propertyDescription_id")
    private PropertyDescription propertyDescription;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "hotel_policy",
    joinColumns = @JoinColumn(name = "hotel_id"),
    inverseJoinColumns = @JoinColumn(name = "policy_id"))
    private Set<Policy> policies = new HashSet<>();

    @OneToMany(mappedBy = "hotel" ,cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "hotel",cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "hotel",cascade = CascadeType.ALL)
    private List<Invoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "hotel",cascade = CascadeType.ALL)
    private List<Editor> editors = new ArrayList<>();

    @OneToMany(mappedBy = "hotel",cascade = CascadeType.ALL)
    private List<HotelAttachment> hotelAttachments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "hotel_promotion",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "promotion_id"))
    private Set<Promotion> promotions = new HashSet<>();

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
