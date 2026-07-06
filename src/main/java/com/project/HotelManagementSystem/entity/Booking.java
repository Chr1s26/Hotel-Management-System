package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.converter.BookingStatusConverter;
import com.project.HotelManagementSystem.converter.CurrencyTypeConverter;
import com.project.HotelManagementSystem.entity.constants.BookingStatus;
import com.project.HotelManagementSystem.entity.constants.CurrencyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking extends MasterData {

    @Column(nullable = false)
    private LocalDateTime bookingDate;//

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "booking_status",nullable = false)
    @Convert(converter = BookingStatusConverter.class)
    private BookingStatus bookingStatus;//

    @Column(nullable = false)
    private int totalNights;

    @Column(nullable = false)
    private boolean paid;

    @Column(nullable = false)
    private double originalTotalPrice;

    @Column(nullable = true)
    private double discountAmount;

    @Column(nullable = true)
    private double taxAmount;

    @Column(nullable = false)
    private double finalTotalPrice;

    @Column(name = "currency_type",nullable = false)
    @Convert(converter = CurrencyTypeConverter.class)
    private CurrencyType currencyType;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = true)
    private String description;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookingGuest leadGuest;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingRoom> bookingRooms = new ArrayList<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookingPreference preference;

    @OneToOne(mappedBy = "booking", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Invoice invoice;

    @OneToOne(mappedBy = "booking", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Payment payment;
}
