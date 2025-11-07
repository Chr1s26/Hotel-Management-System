package com.project.HotelManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "regions",uniqueConstraints = @UniqueConstraint(columnNames = {"name", "country_id"}))
public class Region extends MasterData {

    private String name;

    @OneToMany(mappedBy="region")
    private List<City> cities;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Override
    public String toString() {
        return "Region: " + name+". Country: " + country.getName();
    }
}
