package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.entity.constants.HotelMediaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hotel_attachments")
public class HotelAttachment extends MasterData{

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    private HotelMediaType hotelMediaType;
}
