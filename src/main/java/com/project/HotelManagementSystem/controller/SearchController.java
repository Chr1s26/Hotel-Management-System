package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.dto.booking.HotelSearchDTO;
import com.project.HotelManagementSystem.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public String showSearchPage(Model model){
        model.addAttribute("search", new HotelSearchDTO());
        return "search/home";
    }

    @PostMapping
    public String search(@ModelAttribute("search") HotelSearchDTO dto, Model model){
        model.addAttribute("hotels",searchService.searchHotels(dto));
        model.addAttribute("search",dto);
        return "search/results";
    }
}
