package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.dto.room.RoomCreateDTO;
import com.project.HotelManagementSystem.dto.room.RoomDTO;
import com.project.HotelManagementSystem.dto.room.RoomUpdateDTO;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.service.AmenitiesService;
import com.project.HotelManagementSystem.service.HotelService;
import com.project.HotelManagementSystem.service.PromotionService;
import com.project.HotelManagementSystem.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final HotelService hotelService;
    private final AmenitiesService amenitiesService;
    private final PromotionService promotionService;

    @GetMapping
    public String getAllRooms(Model model) {
        model.addAttribute("rooms", roomService.findAllRooms());
        return "rooms/listing";
    }
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("room", new RoomCreateDTO());
        model.addAttribute("hotels", hotelService.findAllHotels());
        model.addAttribute("amenities", amenitiesService.findAllAmenities());
        model.addAttribute("promotions", promotionService.findAllPromotions());
        return "rooms/create";
    }

    @PostMapping("/create")
    public String createRoom(@ModelAttribute RoomCreateDTO roomCreateDTO) {
        roomService.createRoom(roomCreateDTO);
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.findRoomById(id));
        model.addAttribute("hotels", hotelService.findAllHotels());
        model.addAttribute("amenities", amenitiesService.findAllAmenities());
        model.addAttribute("promotions", promotionService.findAllPromotions());
        return "rooms/edit";
    }

    @PostMapping("/update/{id}")
    public String updateRoom(@PathVariable Long id, @ModelAttribute RoomUpdateDTO RoomUpdateDTO) {
        roomService.updateRoom(id, RoomUpdateDTO);
        return "redirect:/rooms";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/rooms";
    }
}