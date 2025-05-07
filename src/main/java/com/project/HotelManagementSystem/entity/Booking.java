package com.project.HotelManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private LocalDate bookingDate;

    @Column
    private Date checkInDate;

    @Column
    private Date checkOutDate;

    @Column
    private String description;

    @Column
    private String status;
//    confirm cancel complete onprogess

    @Column
    private int numberOfGuests;

    @Column
    private int numberOfRooms;

    @Column
    private boolean isPaid;

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
}
