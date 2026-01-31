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

    public List<Hotel> searchHotels(HotelSearchDTO dto) {

        if (dto.getHotelId() != null) {
            return hotelRepository.findHotelAndNearby(dto.getHotelId());
        }

        if (dto.getCityId() != null) {
            return hotelRepository.findByCityId(dto.getCityId());
        }

        if (dto.getRegionId() != null) {
            return hotelRepository.findByRegionId(dto.getRegionId());
        }

        if (dto.getCountryId() != null) {
            return hotelRepository.findByCountryId(dto.getCountryId());
        }

        return hotelRepository.searchByKeyword(
                dto.getKeyword() == null ? "" : dto.getKeyword().trim()
        );
    }
}
