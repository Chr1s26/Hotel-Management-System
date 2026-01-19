package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.booking.HotelSearchDTO;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final HotelRepository hotelRepository;

    public List<Hotel> searchHotels(HotelSearchDTO dto){
        String keyword = dto.getKeyword();
        return hotelRepository.searchByKeyword(
                keyword == null ? "" : keyword.trim()
        );
    }
}
