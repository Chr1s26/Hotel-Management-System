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
public class PropertyDescription extends MasterData {

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private LocalDate openingDate;

    @Column(nullable = true)
    private LocalDate renovationDate;

    @Column(nullable = true)
    private int numberOfRooms;

    @OneToOne(mappedBy = "propertyDescription")
    private Hotel hotel;

    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PropertyDescription)) return false;
        PropertyDescription other = (PropertyDescription) o;
        return getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
