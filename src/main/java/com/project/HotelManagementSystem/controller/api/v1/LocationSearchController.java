package com.project.HotelManagementSystem.controller.api.v1;

import com.project.HotelManagementSystem.dto.booking.LocationSearchDocument;
import com.project.HotelManagementSystem.service.search.LocationSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/search")
public class LocationSearchController {

    private final LocationSearchService searchService;

    @GetMapping("/autocomplete")
    public List<LocationSearchDocument> autocomplete(@RequestParam String q) throws Exception {
        return searchService.autocomplete(q);
    }
}
