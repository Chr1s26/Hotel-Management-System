//package com.project.HotelManagementSystem.controller.api.v1;
//
//import com.project.HotelManagementSystem.dto.checkout.CheckoutRequest;
//import com.project.HotelManagementSystem.dto.checkout.CheckoutResponse;
//import com.project.HotelManagementSystem.service.CheckoutService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
///** Checkout: turns the cart into a PENDING booking + Stripe PaymentIntent (JWT-protected). */
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/checkout")
//public class CheckoutController {
//
//    private final CheckoutService checkoutService;
//
//    @PostMapping
//    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request) {
//        try {
//            CheckoutResponse response = checkoutService.checkout(request);
//            return ResponseEntity.ok(response);
//        } catch (IllegalStateException e) {
//            // cart empty / mixed hotel-date-currency / room taken
//            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
//        }
//    }
//}
