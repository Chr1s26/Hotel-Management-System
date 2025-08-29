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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking extends MasterData {

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Column(nullable = true)
    private String description;

    @Column(name = "booking_status",nullable = false)
    @Convert(converter = BookingStatusConverter.class)
    private BookingStatus bookingStatus;
//    confirm cancel complete onprogess

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private int numberOfRooms;

    @Column(nullable = false)
    private boolean isPaid;

    @Column(nullable = false)
    private double roomPrice;

    @Column(nullable = true)
    private double tax;

    @Column(nullable = true)
    private double discountPrice;

    @Column(nullable = false)
    private double totalPrice;

    @Column(name = "currency_type",nullable = false)
    @Convert(converter = CurrencyTypeConverter.class)
    private CurrencyType currencyType;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "booking")
    private List<Room> rooms = new ArrayList<>();

    @OneToOne(mappedBy = "booking",cascade = CascadeType.ALL)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(mappedBy = "booking")
    private Payment payment;
}
