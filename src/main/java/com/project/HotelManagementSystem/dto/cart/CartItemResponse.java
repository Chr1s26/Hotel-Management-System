package com.project.HotelManagementSystem.dto.cart;

import com.project.HotelManagementSystem.dto.room.RoomSimpleDTO;
import com.project.HotelManagementSystem.entity.constants.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    public Long itemId;
    public Long roomId;
    public String roomName;
    public Long hotelId;
    public String hotelName;
    public LocalDate checkIn;
    public LocalDate checkOut;
    public int nights;
    public int quantity;
    public double pricePerNight;
    public double totalPrice;
    public CurrencyType currency;
    public boolean available;
}
