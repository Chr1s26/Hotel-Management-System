package com.project.HotelManagementSystem.dto.cart;

import com.project.HotelManagementSystem.dto.room.RoomDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long cartId;
    private Double totalPrice = 0.0;
    private List<RoomDTO> rooms = new ArrayList<>();
}
