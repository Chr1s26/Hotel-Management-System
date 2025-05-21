package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id,Room room) {
        Optional<Room> roomOp = roomRepository.findById(id);
        if (roomOp.isPresent()) {
            Room updatedRoom = roomOp.get();
            updatedRoom.setPrice(room.getPrice());
            updatedRoom.setAvailable(room.isAvailable());
            updatedRoom.setFloor(room.getFloor());
            updatedRoom.setRoomType(room.getRoomType());
            updatedRoom.setMaxCapacity(room.getMaxCapacity());
            return roomRepository.save(updatedRoom);
        }
        return null;
    }

    public void deleteRoom(Long id) {
        Optional<Room> roomOp = roomRepository.findById(id);
        if (roomOp.isPresent()) {
            roomRepository.deleteById(id);
        }
    }

    public Optional<Room> findRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }
}