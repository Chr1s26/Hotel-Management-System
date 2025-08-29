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
@Table(name = "countries")
public class Country extends MasterData {

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "country")
    private List<Region> regions;

    @Override
    public String toString() {
        return name;
    }
}
