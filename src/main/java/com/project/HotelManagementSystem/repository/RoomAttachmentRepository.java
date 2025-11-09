package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.RoomAttachment;
import com.project.HotelManagementSystem.entity.constants.RoomMediaType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomAttachmentRepository extends JpaRepository<RoomAttachment, Long> {
    List<RoomAttachment> findByRoomIdAndRoomMediaType(Long roomId, RoomMediaType roomMediaType);
}
