package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.room.RoomCreateDTO;
import com.project.HotelManagementSystem.dto.room.RoomDTO;
import com.project.HotelManagementSystem.dto.room.RoomResponse;
import com.project.HotelManagementSystem.dto.room.RoomUpdateDTO;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.room.RoomSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.room.RoomSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.room.RoomSearchQuery;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.service.AmenitiesService;
import com.project.HotelManagementSystem.service.HotelService;
import com.project.HotelManagementSystem.service.PromotionService;
import com.project.HotelManagementSystem.service.RoomService;
import com.project.HotelManagementSystem.service.search.RoomSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final RoomSearchService roomSearchService;

    @ModelAttribute("query")
    public RoomSearchQuery initQuery() {
        RoomSearchQuery query = new RoomSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new RoomSearchFilter(RoomSearchField.PRICE, MatchType.EXACT,""),
                new RoomSearchFilter(RoomSearchField.IS_AVAILABLE, MatchType.EXACT,null),
                new RoomSearchFilter(RoomSearchField.STATUS, MatchType.EXACT,""),
                new RoomSearchFilter(RoomSearchField.ROOM_TYPE,MatchType.EXACT,""),
                new RoomSearchFilter(RoomSearchField.MAX_CAPACITY,MatchType.EXACT,"")
        ));
        return query;
    }

    @GetMapping
    public String getAllRooms(Model model, @ModelAttribute("query") RoomSearchQuery query) {
        Page<Room> page = roomSearchService.searchByQuery(query);
        model.addAttribute("rooms",page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "rooms/listing";
    }

    @PostMapping
    public String searchRooms(Model model, @ModelAttribute("query") RoomSearchQuery query) {
        Page<Room> page = roomSearchService.searchByQuery(query);
        model.addAttribute("rooms",page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
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