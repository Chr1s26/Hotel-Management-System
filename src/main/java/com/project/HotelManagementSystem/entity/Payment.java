package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.converter.CurrencyTypeConverter;
import com.project.HotelManagementSystem.converter.PaymentStatusConverter;
import com.project.HotelManagementSystem.entity.constants.CurrencyType;
import com.project.HotelManagementSystem.entity.constants.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double amount;

    @Column
    private String payment_method;

    @Column
    private LocalDateTime payment_date;

    @Column(name = "payment_status")
    @Convert(converter = PaymentStatusConverter.class)
    private PaymentStatus paymentStatus;

    @Column(name = "currency_type")
    @Convert(converter = CurrencyTypeConverter.class)
    private CurrencyType currencyType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "payment")
    private Invoice invoice;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
