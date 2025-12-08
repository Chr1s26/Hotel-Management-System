package com.project.HotelManagementSystem.controller.api.v1;

import com.project.HotelManagementSystem.dto.document.room.RoomSearchResponse;
import com.project.HotelManagementSystem.service.search.RoomSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/rooms")
public class RoomSearchController {
    private final RoomSearchService roomSearchService;

    public RoomSearchController(RoomSearchService roomSearchService) {
        this.roomSearchService = roomSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<RoomSearchResponse> search(@RequestParam("field") String field, @RequestParam(value = "pageIndex",defaultValue = "0") Integer pageIndex, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize) throws IOException {
        RoomSearchResponse rooms = this.roomSearchService.elasticSearchByQuery(field,pageIndex,pageSize);
        return ResponseEntity.ok(rooms);
    }
}
