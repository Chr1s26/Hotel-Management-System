package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.room.RoomSearchQuery;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.entity.specification.RoomSpecification;
import com.project.HotelManagementSystem.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomSearchService {

    private final CommonSearchService commonSearchService;
    private final RoomRepository roomRepository;

    public Page<Room> searchByQuery(RoomSearchQuery query) {
        return commonSearchService.searchByQuery(roomRepository, RoomSpecification::fromFilter,query);
    }

    public List<Room> searchByQueryAll(RoomSearchQuery query) {
        return commonSearchService.searchByQueryAll(roomRepository, RoomSpecification::fromFilter,query);
    }
}
