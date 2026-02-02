package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.service.search.LocationIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reindex")
public class IndexController {

    private final LocationIndexService locationIndexService;

    @GetMapping
    public String indexPage() {
        return "index";
    }

    @PostMapping
    public String reindexAll() throws Exception{
        locationIndexService.reindexAllLocations();
        return "redirect:/reindex?success";
    }
}
