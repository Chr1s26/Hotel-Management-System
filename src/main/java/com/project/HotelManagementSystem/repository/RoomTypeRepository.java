package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType,Long> {
    Optional<RoomType> findByNameAndPriceAndRoomSizeAndCapacity(String name, double price, int roomSize, int capacity);
}
