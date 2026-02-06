//package com.project.HotelManagementSystem.controller;
//
//import com.project.HotelManagementSystem.dto.booking.BookingRequestDTO;
//import com.project.HotelManagementSystem.dto.booking.BookingSummaryDTO;
//import com.project.HotelManagementSystem.service.BookingPageService;
//import com.project.HotelManagementSystem.service.BookingService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/bookings")
//public class BookingController {
//    private final BookingPageService bookingPageService;
//    private final BookingService bookingService;
//
//    @GetMapping
//    public String bookingPage(@RequestParam Long hotelId, @RequestParam Long roomId, @RequestParam LocalDate checkIn, @RequestParam LocalDate checkOut, Model model) {
//        BookingSummaryDTO summary = bookingPageService.prepareBooking(hotelId, roomId, checkIn, checkOut);
//
//        model.addAttribute("summary", summary);
//        model.addAttribute("bookingRequest", new BookingRequestDTO());
//
//        return "booking/booking-page";
//    }
//
//    @PostMapping("/confirm")
//    public String confirmBooking(@ModelAttribute BookingRequestDTO request) {
//        Long bookingId = bookingService.createBooking(request);
//        return "redirect:/payment?bookingId=" + bookingId;
//    }
//
//}
