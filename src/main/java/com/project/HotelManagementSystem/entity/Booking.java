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
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime bookingDate;

    @Column
    private LocalDate checkInDate;

    @Column
    private LocalDate checkOutDate;

    @Column
    private String description;

    @Column(name = "booking_status")
    @Convert(converter = BookingStatusConverter.class)
    private BookingStatus bookingStatus;
//    confirm cancel complete onprogess

    @Column
    private int numberOfGuests;

    @Column
    private int numberOfRooms;

    @Column
    private boolean isPaid;

    @Column
    private double roomPrice;

    @Column
    private double tax;

    @Column
    private double discountPrice;

    @Column
    private double totalPrice;

    @Column(name = "currency_type")
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
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "booking")
    private Payment payment;
}
