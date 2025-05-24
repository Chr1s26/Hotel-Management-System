package com.project.HotelManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column
    private String road;

    @OneToOne(mappedBy = "address")
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Column
    private String zipCode;

    @Override
    public String toString() {
        return " Road = "+road+" Lat = "+latitude+" Long = "+longitude+"Zip Code = "+zipCode;
    }
}
