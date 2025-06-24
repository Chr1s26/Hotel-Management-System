package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.room.RoomCreateDTO;
import com.project.HotelManagementSystem.dto.room.RoomDTO;
import com.project.HotelManagementSystem.dto.room.RoomResponse;
import com.project.HotelManagementSystem.dto.room.RoomUpdateDTO;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.repository.AmenitiesRepository;
import com.project.HotelManagementSystem.repository.PromotionRepository;
import com.project.HotelManagementSystem.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final AmenitiesRepository amenitiesRepository;
    private final PromotionRepository promotionRepository;

    @Autowired
    private ModelMapper modelMapper;

    public RoomCreateDTO createRoom(RoomCreateDTO roomCreateDTO) {
        Room room = modelMapper.map(roomCreateDTO, Room.class);
        room.setAmenities(new HashSet<>(amenitiesRepository.findAllById(roomCreateDTO.getAmenityIds())));
        room.setPromotions(new HashSet<>(promotionRepository.findAllById(roomCreateDTO.getPromotionIds())));
        Room savedRoom = roomRepository.save(room);
        return modelMapper.map(savedRoom, RoomCreateDTO.class);
    }

    public RoomUpdateDTO updateRoom(Long id, RoomUpdateDTO roomUpdateDTO) {
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
            return modelMapper.map(savedRoom, RoomUpdateDTO.class);
        }
        return null;
    }

    public void deleteRoom(Long id) {
        Optional<Room> roomOp = roomRepository.findById(id);
        if (roomOp.isPresent()) {
            roomRepository.deleteById(id);
        }
    }

    public RoomDTO findRoomById(Long id) {
        Room room = roomRepository.findById(id).orElse(null);
        return modelMapper.map(room, RoomDTO.class);
    }

    public List<RoomDTO> findAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(room -> modelMapper.map(room, RoomDTO.class)).collect(Collectors.toList());
    }

    public RoomResponse findAllRoomsWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndSortOrder);
        Page<Room> roomPage = roomRepository.findAll(pageable);
        List<Room> roomList = roomPage.getContent();
        List<RoomDTO> roomDTOList = roomList.stream().map(room -> modelMapper.map(room, RoomDTO.class)).toList();
        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setRooms(roomDTOList);
        roomResponse.setPageNumber(roomPage.getNumber());
        roomResponse.setPageSize(roomPage.getSize());
        roomResponse.setTotalPages(roomPage.getTotalPages());
        roomResponse.setTotalElements(roomPage.getTotalElements());
        roomResponse.setLastPage(roomPage.isLast());
        return  roomResponse;
    }
}