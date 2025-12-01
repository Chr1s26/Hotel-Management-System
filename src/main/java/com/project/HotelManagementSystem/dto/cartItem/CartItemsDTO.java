package com.project.HotelManagementSystem.dto.cartItem;

import com.project.HotelManagementSystem.dto.cart.CartDTO;
import com.project.HotelManagementSystem.dto.room.RoomDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemsDTO {
    public Long cartItemId;
    private CartDTO cartDTO;
    private RoomDTO roomDTO;
    private Integer quantity;
    private Double discount;
    private Double roomPrice;
}
