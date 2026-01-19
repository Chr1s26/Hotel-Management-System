package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.dto.booking.BookingCreateDTO;
import com.project.HotelManagementSystem.entity.Booking;
import com.project.HotelManagementSystem.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/create")
    public String createBooking(@ModelAttribute BookingCreateDTO dto, Model model) {

        Booking booking = bookingService.createBooking(dto);
        return "redirect:/bookings/confirm/" + booking.getId();
    }

    @GetMapping("/confirm/{id}")
    public String confirm(@PathVariable Long id, Model model){
        model.addAttribute("booking", bookingService.findById(id));
        return "bookings/confirm";
    }

}
