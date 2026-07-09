//package com.project.HotelManagementSystem.controller.api.v1;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.HotelManagementSystem.dto.booking.HotelSearchDTO;
//import com.project.HotelManagementSystem.dto.booking.HotelSearchResultDTO;
//import com.project.HotelManagementSystem.exception.InvalidSearchException;
//import com.project.HotelManagementSystem.repository.AmenitiesRepository;
//import com.project.HotelManagementSystem.repository.RoomTypeRepository;
//import com.project.HotelManagementSystem.service.cache.HotelSearchCacheService;
//import com.project.HotelManagementSystem.service.search.elasticSearch.SearchService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/public/search")
//@SessionAttributes("search")
//public class HotelSearchController {
//
//
//    private final SearchService searchService;
//    private final HotelSearchCacheService cacheService;
//    private final RoomTypeRepository roomTypeRepository;
//    private final AmenitiesRepository amenitiesRepository;
//    private final ObjectMapper objectMapper;
//
//    /** Filter options for the results UI (room types + amenities). */
//    @GetMapping("/filters")
//    public ResponseEntity<Map<String, Object>> filters() {
//        return ResponseEntity.ok(Map.of(
//                "roomTypes", roomTypeRepository.findDistinctRoomTypeNames(),
//                "amenities", amenitiesRepository.findAll()
//        ));
//    }
//
//    /** Run a search. Cache-first: identical payloads within the TTL are served from Redis. */
//    @PostMapping("/results")
//    public ResponseEntity<?> search(@RequestBody HotelSearchDTO dto) {
//        final String cacheKey;
//        try {
//            cacheKey = "hotel-search:" + objectMapper.writeValueAsString(dto);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Invalid search payload"));
//        }
//
//        List<HotelSearchResultDTO> cached = cacheService.get(cacheKey);
//        if (cached != null) {
//            return ResponseEntity.ok(cached);
//        }
//
//        try {
//            List<HotelSearchResultDTO> results = searchService.search(dto);
//            cacheService.set(cacheKey, results);
//            return ResponseEntity.ok(results);
//        } catch (InvalidSearchException e) {
//            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", "Search failed"));
//        }
//    }
//}
