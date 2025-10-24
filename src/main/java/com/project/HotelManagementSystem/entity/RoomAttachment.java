package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.entity.constants.RoomMediaType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_attachments")
public class RoomAttachment extends MasterData{

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    private RoomMediaType roomMediaType;
}
