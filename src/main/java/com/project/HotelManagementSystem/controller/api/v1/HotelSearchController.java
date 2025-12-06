package com.project.HotelManagementSystem.controller.api.v1;

import com.project.HotelManagementSystem.dto.document.hotel.HotelSearchDocument;
import com.project.HotelManagementSystem.dto.searchFilter.hotel.HotelSearchQuery;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.service.search.HotelSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelSearchController {
    private final HotelSearchService hotelSearchService;

    @GetMapping("/search")
    public List<HotelSearchDocument> searchHotels(@RequestParam("hotelName") String hotelName) throws IOException {
        List<HotelSearchDocument> hotels = this.hotelSearchService.elasticSearchByQuery(hotelName);
        return hotels;
    }
}
