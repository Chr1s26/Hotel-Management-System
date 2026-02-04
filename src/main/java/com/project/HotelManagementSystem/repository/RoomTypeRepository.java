package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType,Long> {
    Optional<RoomType> findByNameAndPriceAndRoomSizeAndCapacity(String name, double price, int roomSize, int capacity);
    @Query("SELECT DISTINCT rt.name FROM RoomType rt")
    List<String> findDistinctRoomTypeNames();

}
