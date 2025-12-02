package com.project.HotelManagementSystem.repository.document;

import com.project.HotelManagementSystem.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemV2Repository extends JpaRepository<CartItem,Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.room.id = ?1 AND ci.cart.id = ?2")
    CartItem findCartItemByRoomIdAndCartId(Long roomId, Long cartId);
}
