package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.entity.constants.CurrencyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cartItems")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate checkInDate;

    @Column
    private LocalDate checkOutDate;

    @Column
    private int quantity;

    @Column
    private double price;

    @Column(name = "currency_type")
    private CurrencyType currencyType;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
