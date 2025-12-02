package com.project.HotelManagementSystem.repository.document;

import com.project.HotelManagementSystem.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartV2Repository extends JpaRepository<Cart,Long> {
    @Query("SELECT c FROM Cart c where c.user.email = ?1")
    Cart findByEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.id = ?2")
    Cart findCartByEmailAndCartId(String email, Long cartId);
}
