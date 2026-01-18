package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.RoomType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final AuthService authService;

    public RoomType findOrCreate(String name, double price, int roomSize, int capacity){
        Optional<RoomType> roomTypeOp = roomTypeRepository.findByNameAndPriceAndRoomSizeAndCapacity(name,price,roomSize,capacity);
        if(roomTypeOp.isEmpty()){
            RoomType rt = new RoomType();
            rt.setName(name);
            rt.setPrice(price);
            rt.setRoomSize(roomSize);
            rt.setCapacity(capacity);
            rt.setStatus(StatusType.ACTIVE);
            rt.setCreatedAt(LocalDateTime.now());
            rt.setUpdatedAt(LocalDateTime.now());
            rt.setCreatedBy(authService.getCurrentUser());
            rt.setUpdatedBy(authService.getCurrentUser());
            return roomTypeRepository.save(rt);
        }
        return roomTypeOp.get();
    }
}
