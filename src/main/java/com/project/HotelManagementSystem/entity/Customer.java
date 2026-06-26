package com.project.HotelManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends UserMasterData{

    @Column(nullable = true)
    private int point;

    @Column(nullable = false)
    private boolean vipStatus;

    @OneToMany(mappedBy = "customer")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Invoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings = new ArrayList<>();
    //need to delete
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "customer_promotion",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "promotion_id"))
    private Set<Promotion> promotions = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private User user;

}
