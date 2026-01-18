package com.project.HotelManagementSystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "room_types")
public class RoomType extends MasterData{

    @Column
    private String name;
    @Column
    private int capacity;
    @Column
    private int roomSize;
    @Column
    private double price;
    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms = new ArrayList<>();
}
