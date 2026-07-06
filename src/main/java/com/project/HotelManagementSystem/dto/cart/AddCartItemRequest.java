package com.project.HotelManagementSystem.dto.cart;

import com.project.HotelManagementSystem.entity.constants.CurrencyType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class AddCartItemRequest {
    private Long roomId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) private LocalDate checkIn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) private LocalDate checkOut;
    private int quantity = 1;
    private CurrencyType currency = CurrencyType.USD;

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public CurrencyType getCurrency() { return currency; }
    public void setCurrency(CurrencyType currency) { this.currency = currency; }
}
