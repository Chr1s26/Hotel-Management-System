package com.project.HotelManagementSystem.controller.api.v1;

import com.project.HotelManagementSystem.dto.booking.HotelSearchDTO;
import com.project.HotelManagementSystem.dto.booking.HotelSearchResultDTO;
import com.project.HotelManagementSystem.exception.InvalidSearchException;
import com.project.HotelManagementSystem.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/search")
public class HotelSearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public String showSearchPage(Model model){
        model.addAttribute("search", new HotelSearchDTO());
        return "search/home";
    }

    @PostMapping("/results")
    public String search(@ModelAttribute("search") HotelSearchDTO dto, Model model) throws Exception {
        try {
            List<HotelSearchResultDTO> results = searchService.search(dto);
            model.addAttribute("hotels", results);
        } catch (InvalidSearchException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("search", dto);
        return "search/results";
    }
}
