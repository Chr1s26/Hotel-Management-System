package com.project.HotelManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingPreference extends MasterData{
    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(nullable = true)
    private String smokingPreference;

    @Column(nullable = true)
    private String bedPreference;
}
