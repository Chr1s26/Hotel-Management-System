package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomAmenitiesService {

    private final RoomService roomService;
    private final AmenitiesService amenitiesService;
    private final RoomRepository roomRepository;
//
//    public Room addAmenitiesToRoom(Long roomId,Long amenityId) {
////        Room room = roomService.findRoomById(roomId);
////        Amenities amenities = amenitiesService.findAmenitiesById(amenityId);
////        room.getAmenities().add(amenities);
//        return roomRepository.save(room);
//    }
}
