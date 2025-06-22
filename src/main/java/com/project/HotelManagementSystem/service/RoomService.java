package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.room.RoomCreateDTO;
import com.project.HotelManagementSystem.dto.room.RoomDTO;
import com.project.HotelManagementSystem.dto.room.RoomUpdateDTO;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.repository.AmenitiesRepository;
import com.project.HotelManagementSystem.repository.PromotionRepository;
import com.project.HotelManagementSystem.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final AmenitiesRepository amenitiesRepository;
    private final PromotionRepository promotionRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Room createRoom(RoomCreateDTO roomCreateDTO) {
        Room room = modelMapper.map(roomCreateDTO, Room.class);
        room.setAmenities(new HashSet<>(amenitiesRepository.findAllById(roomCreateDTO.getAmenityIds())));
        room.setPromotions(new HashSet<>(promotionRepository.findAllById(roomCreateDTO.getPromotionIds())));
        Room savedRoom = roomRepository.save(room);
        return modelMapper.map(savedRoom, Room.class);
    }

    public RoomDTO updateRoom(Long id, RoomUpdateDTO roomUpdateDTO) {
        Optional<Room> roomOp = roomRepository.findById(id);
        Room room = modelMapper.map(roomUpdateDTO, Room.class);
        if (roomOp.isPresent()) {
            Room updatedRoom = roomOp.get();
            updatedRoom.setPrice(room.getPrice());
            updatedRoom.setAvailable(room.isAvailable());
            updatedRoom.setDescription(room.getDescription());
            updatedRoom.setRoomType(room.getRoomType());
            updatedRoom.setMaxCapacity(room.getMaxCapacity());
            updatedRoom.setHotel(room.getHotel());
            updatedRoom.setAmenities(new HashSet<>(amenitiesRepository.findAllById(roomUpdateDTO.getAmenityIds())));
            updatedRoom.setPromotions(new HashSet<>(promotionRepository.findAllById(roomUpdateDTO.getPromotionIds())));
            Room savedRoom = roomRepository.save(updatedRoom);
            return modelMapper.map(savedRoom, RoomDTO.class);
        }
        return null;
    }

    public void deleteRoom(Long id) {
        Optional<Room> roomOp = roomRepository.findById(id);
        if (roomOp.isPresent()) {
            roomRepository.deleteById(id);
        }
    }

    public RoomUpdateDTO findRoomById(Long id) {
        Room room = roomRepository.findById(id).orElse(null);
        return modelMapper.map(room, RoomUpdateDTO.class);
    }

    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }
}