//package com.project.HotelManagementSystem.controller.api.v1;
//
//import com.project.HotelManagementSystem.dto.document.room.RoomSearchResponse;
//import com.project.HotelManagementSystem.dto.searchFilter.room.RoomSearchQuery;
//import com.project.HotelManagementSystem.service.search.RoomSearchService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("api/v1/rooms")
//public class RoomSearchController {
//    private final RoomSearchService roomSearchService;
//
//    public RoomSearchController(RoomSearchService roomSearchService) {
//        this.roomSearchService = roomSearchService;
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<RoomSearchResponse> search(@RequestBody RoomSearchQuery query) throws IOException {
//        RoomSearchResponse rooms = this.roomSearchService.elasticSearchByQuery(query);
//        return ResponseEntity.ok(rooms);
//    }
//}
