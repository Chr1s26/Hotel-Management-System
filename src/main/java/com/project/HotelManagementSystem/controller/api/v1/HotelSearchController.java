package com.project.HotelManagementSystem.controller.api.v1;

import com.project.HotelManagementSystem.dto.document.hotel.HotelSearchResponse;
import com.project.HotelManagementSystem.service.search.HotelSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelSearchController {
    private final HotelSearchService hotelSearchService;

    @GetMapping("/search")
    public ResponseEntity<HotelSearchResponse> searchHotels(@RequestParam("hotelName") String hotelName, @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) throws IOException {
        HotelSearchResponse hotels = this.hotelSearchService.elasticSearchByQuery(hotelName, pageIndex, pageSize);
        return ResponseEntity.ok(hotels);
    }
}
