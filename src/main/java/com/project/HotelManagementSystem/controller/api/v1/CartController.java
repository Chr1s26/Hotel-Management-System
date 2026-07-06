package com.project.HotelManagementSystem.controller.api.v1;

import com.project.HotelManagementSystem.dto.cart.AddCartItemRequest;
import com.project.HotelManagementSystem.dto.cart.CartResponse;
import com.project.HotelManagementSystem.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Customer cart (JWT-protected — not under /public). */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@RequestBody AddCartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(request));
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> updateQuantity(@PathVariable Long itemId,
                                                       @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateQuantity(itemId, quantity));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItem(itemId));
    }
}
