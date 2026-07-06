package com.project.HotelManagementSystem.dto.cart;

import com.project.HotelManagementSystem.entity.constants.CurrencyType;

import java.util.List;

public class CartResponse {
    public Long cartId;
    public List<CartItemResponse> items;
    public double grandTotal;
    public CurrencyType currency;
    public boolean checkoutReady;
    public String blockReason;
}
