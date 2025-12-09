package com.project.HotelManagementSystem.controller.api.v1;

import com.project.HotelManagementSystem.dto.document.hotel.HotelSearchResponse;
import com.project.HotelManagementSystem.dto.searchFilter.hotel.HotelSearchQuery;
import com.project.HotelManagementSystem.service.search.HotelSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelSearchController {
    private final HotelSearchService hotelSearchService;

    @GetMapping("/search")
    public ResponseEntity<HotelSearchResponse> searchHotels(@RequestBody HotelSearchQuery query) throws IOException {
        HotelSearchResponse hotels = this.hotelSearchService.elasticSearchByQuery(query);
        return ResponseEntity.ok(hotels);
    }
}
