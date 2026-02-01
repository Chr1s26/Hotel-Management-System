//package com.project.HotelManagementSystem.controller.api.v1;
//
//import com.project.HotelManagementSystem.dto.document.hotel.HotelSearchResponse;
//import com.project.HotelManagementSystem.dto.searchFilter.hotel.HotelSearchField;
//import com.project.HotelManagementSystem.dto.searchFilter.hotel.HotelSearchFilter;
//import com.project.HotelManagementSystem.dto.searchFilter.hotel.HotelSearchQuery;
//import com.project.HotelManagementSystem.service.search.HotelSearchService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/private/hotels")
//@RequiredArgsConstructor
//public class HotelSearchController {
//    private final HotelSearchService hotelSearchService;
//
//    @GetMapping("/search")
//    public ResponseEntity<HotelSearchResponse> searchHotels(@RequestBody HotelSearchQuery query) throws IOException {
//        HotelSearchResponse hotels = this.hotelSearchService.elasticSearchByQuery(query);
//        return ResponseEntity.ok(hotels);
//    }
//
//    @GetMapping("/autocomplete-search")
//    public ResponseEntity<HotelSearchResponse> autocomplete(@RequestParam("q") String q, @RequestParam(name="field", defaultValue = "NAME") HotelSearchField field) throws IOException {
//        HotelSearchQuery hotelSearchQuery = new HotelSearchQuery();
//        hotelSearchQuery.setFilterList(List.of(new HotelSearchFilter(field, q)));
//        hotelSearchQuery.setPageNumber(0);
//        hotelSearchQuery.setPageSize(20);
//        HotelSearchResponse hotels = this.hotelSearchService.elasticSearchByQuery(hotelSearchQuery);
//        return ResponseEntity.ok(hotels);
//    }
//}
