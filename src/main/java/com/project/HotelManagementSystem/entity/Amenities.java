package com.project.HotelManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "amenities")
public class Amenities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private boolean isWifiAvailable;

    @Column
    private boolean breakfast;

    @Column
    private boolean spa;

    @Column
    private boolean cleaningService;

    @Column
    private boolean laundryService;

    @OneToMany(mappedBy = "amenities")
    private List<Room> room = new ArrayList<>();
}
