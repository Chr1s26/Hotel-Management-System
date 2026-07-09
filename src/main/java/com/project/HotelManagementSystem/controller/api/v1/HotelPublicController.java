//package com.project.HotelManagementSystem.controller.api.v1;
//
//import com.project.HotelManagementSystem.dto.booking.HotelDetailPageDTO;
//import com.project.HotelManagementSystem.dto.booking.HotelSearchDTO;
//import com.project.HotelManagementSystem.entity.Customer;
//import com.project.HotelManagementSystem.entity.User;
//import com.project.HotelManagementSystem.repository.CustomerRepository;
//import com.project.HotelManagementSystem.service.AuthService;
//import com.project.HotelManagementSystem.service.HotelDetailPageService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/public")
//public class HotelPublicController {
//    private final HotelDetailPageService hotelDetailPageService;
//    private final CustomerRepository customerRepository;
//    private final AuthService authService;
//
//    @GetMapping("/hotel/{hotelId}")
//    public ResponseEntity<HotelDetailPageDTO> hotelDetail(
//            @PathVariable Long hotelId,
//            @RequestParam(value = "checkIn", required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn) {
//
//        Long customerId = resolveCurrentCustomerId();
//        HotelDetailPageDTO hotel = hotelDetailPageService.getHotelDetailPage(hotelId, customerId, checkIn);
//        return ResponseEntity.ok(hotel);
//    }
//
//    /** Returns the signed-in customer's id, or null for anonymous visitors. */
//    private Long resolveCurrentCustomerId() {
//        try {
//            User user = authService.getCurrentUser();
//            if (user == null) return null;
//            return customerRepository.findByUser(user).map(Customer::getId).orElse(null);
//        } catch (Exception e) {
//            return null; // anonymous
//        }
//    }
//}
