package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.converter.DiscountTypeConverter;
import com.project.HotelManagementSystem.entity.constants.DiscountType;
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
@Table(name = "promotions")
public class Promotion extends MasterData {

    @Column
    private String code;

    @Column(name = "discount_type", nullable = false)
    @Convert(converter = DiscountTypeConverter.class)
    private DiscountType discountType; //percentage or normal

    @Column(nullable = false)
    private double discountAmount;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = true)
    private int pointAmount;

    @Column(nullable = false)
    private int usageLimit;

    @Column(nullable = true)
    private int timesUsed;

    @ManyToMany(mappedBy = "promotions")
    private Set<Customer> customers = new HashSet<>();

    @ManyToMany(mappedBy = "promotions")
    private Set<Room> rooms = new HashSet<>();

    @ManyToMany(mappedBy = "promotions")
    private Set<Hotel> hotels = new HashSet<>();

    @OneToMany(mappedBy = "promotion")
    private List<CartItem> cartItems = new ArrayList<>();
}
