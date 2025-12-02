package com.project.HotelManagementSystem.controller.api.v1;

import com.project.HotelManagementSystem.dto.cart.CartDTO;
import com.project.HotelManagementSystem.entity.Cart;
import com.project.HotelManagementSystem.repository.document.CartV2Repository;
import com.project.HotelManagementSystem.service.CartService;
import com.project.HotelManagementSystem.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartV2Repository cartRepository;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/carts/room/{roomId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long roomId,
                                                    @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addRoomToCart(roomId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts(){
        List<CartDTO> carts = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(carts, HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById(){
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findByEmail(emailId);
        Long cartId = cart.getId();
        CartDTO cartDTO = cartService.getCart(emailId, cartId);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/carts/room/{roomId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long roomId,@PathVariable String operation){
        CartDTO cartDTO = cartService.updateRoomQuantityInCart(roomId,operation.equalsIgnoreCase("delete") ? -1:1);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }
}
