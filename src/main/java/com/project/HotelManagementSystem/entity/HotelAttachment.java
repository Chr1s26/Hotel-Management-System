package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.entity.constants.HotelMediaType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hotel_attachments")
public class HotelAttachment extends MasterData{
    private Hotel hotel;
    private HotelMediaType hotelMediaType;
}
