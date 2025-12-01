package com.project.HotelManagementSystem.repository.document;

import com.project.HotelManagementSystem.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemV2Repository extends JpaRepository<CartItem,Long> {
}
