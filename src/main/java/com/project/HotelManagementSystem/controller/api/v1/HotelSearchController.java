package com.project.HotelManagementSystem.controller.api.v1;

import com.project.HotelManagementSystem.dto.booking.HotelSearchDTO;
import com.project.HotelManagementSystem.dto.booking.HotelSearchResultDTO;
import com.project.HotelManagementSystem.exception.InvalidSearchException;
import com.project.HotelManagementSystem.repository.AmenitiesRepository;
import com.project.HotelManagementSystem.repository.RoomRepository;
import com.project.HotelManagementSystem.repository.RoomTypeRepository;
import com.project.HotelManagementSystem.service.search.elasticSearch.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/search")
@SessionAttributes("search")
public class HotelSearchController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private AmenitiesRepository amenitiesRepository;


    @ModelAttribute("search")
    public HotelSearchDTO searchDTO() {
        return new HotelSearchDTO();
    }

    @GetMapping
    public String showSearchPage(Model model){
        return "search/home";
    }

    @PostMapping("/results")
    public String search(@ModelAttribute("search") HotelSearchDTO dto, Model model) throws Exception {
        try {
            List<HotelSearchResultDTO> results = searchService.search(dto);
            model.addAttribute("roomTypes", roomTypeRepository.findDistinctRoomTypeNames());
            model.addAttribute("amenities", amenitiesRepository.findAll());
            model.addAttribute("hotels", results);
        } catch (InvalidSearchException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("search", dto);
        return "search/results";
    }
}
