package com.project.HotelManagementSystem.repository;
import com.project.HotelManagementSystem.entity.HotelAttachment;
import com.project.HotelManagementSystem.entity.constants.HotelMediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelAttachmentRepository extends JpaRepository<HotelAttachment, Long> {
    List<HotelAttachment> findByHotelIdAndHotelMediaType(Long hotelId, HotelMediaType hotelMediaType);
    Optional<HotelAttachment> findTopByHotelIdAndHotelMediaTypeOrderByCreatedAtDesc(Long hotelId, HotelMediaType hotelMediaType);
    List<HotelAttachment> findTop5ByHotel_IdOrderByCreatedAtDesc(Long hotelId);
}
