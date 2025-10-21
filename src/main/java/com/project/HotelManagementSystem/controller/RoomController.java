package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.room.RoomCreateDTO;
import com.project.HotelManagementSystem.dto.room.RoomDTO;
import com.project.HotelManagementSystem.dto.room.RoomResponse;
import com.project.HotelManagementSystem.dto.room.RoomUpdateDTO;
import com.project.HotelManagementSystem.service.AmenitiesService;
import com.project.HotelManagementSystem.service.HotelService;
import com.project.HotelManagementSystem.service.PromotionService;
import com.project.HotelManagementSystem.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final HotelService hotelService;
    private final AmenitiesService amenitiesService;
    private final PromotionService promotionService;

    @GetMapping
    public String getAllRooms(Model model,
                              @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                              @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                              @RequestParam(defaultValue = AppConstants.SORT_BY_Id) String sortBy,
                              @RequestParam(defaultValue = AppConstants.SORT_ORDER) String sortOrder) {
        RoomResponse roomResponse = roomService.findAllRoomsWithPagination(pageNumber,pageSize,sortBy,sortOrder);
        List<RoomDTO> roomDTOList = roomResponse.getRooms();
        model.addAttribute("rooms",roomDTOList);
        model.addAttribute("response",roomResponse);
        model.addAttribute("sortBy",sortBy);
        model.addAttribute("sortOrder",sortOrder);
        return "rooms/listing";
    }
    @GetMapping("/new")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showCreateForm(Model model) {
        model.addAttribute("room", new RoomCreateDTO());
        model.addAttribute("hotels", hotelService.findAllHotels());
        model.addAttribute("amenities", amenitiesService.findAllAmenities());
        model.addAttribute("promotions", promotionService.findAllPromotions());
        return "rooms/create";
    }

    @PostMapping("/create")
    public String createRoom(@Valid @ModelAttribute("room") RoomCreateDTO roomCreateDTO, BindingResult bindingResult,Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("hotels", hotelService.findAllHotels());
            model.addAttribute("amenities", amenitiesService.findAllAmenities());
            model.addAttribute("promotions", promotionService.findAllPromotions());
            return "rooms/create";
        }
        roomService.createRoom(roomCreateDTO);
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.findRoomById(id));
        model.addAttribute("hotels", hotelService.findAllHotels());
        model.addAttribute("amenities", amenitiesService.findAllAmenities());
        model.addAttribute("promotions", promotionService.findAllPromotions());
        return "rooms/edit";
    }

    @PostMapping("/update/{id}")
    public String updateRoom(@PathVariable Long id,@Valid @ModelAttribute("room") RoomUpdateDTO RoomUpdateDTO,BindingResult bindingResult,Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("hotels", hotelService.findAllHotels());
            model.addAttribute("amenities", amenitiesService.findAllAmenities());
            model.addAttribute("promotions", promotionService.findAllPromotions());
            return "rooms/edit";
        }
        roomService.updateRoom(id, RoomUpdateDTO);
        return "redirect:/rooms";
    }

    @GetMapping("/delete/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/rooms";
    }
}