package com.project.HotelManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "propertyDescriptions")
public class PropertyDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column
    private LocalDate openingDate;

    @Column
    private LocalDate renovationDate;

    @Column
    private int numberOfRooms;

    @OneToOne(mappedBy = "propertyDescription")
    private Hotel hotel;

    @Override
    public String toString() {
        return description;
    }
}
