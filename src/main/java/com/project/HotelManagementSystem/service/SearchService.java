package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.booking.HotelSearchDTO;
import com.project.HotelManagementSystem.dto.booking.HotelSearchResultDTO;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.entity.constants.HotelMediaType;
import com.project.HotelManagementSystem.repository.HotelAttachmentRepository;
import com.project.HotelManagementSystem.repository.HotelRepository;
import com.project.HotelManagementSystem.service.search.HotelSearchElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final HotelRepository hotelRepository;
    private final HotelAttachmentRepository hotelAttachmentRepository;
    private final PricingService pricingService;
    private final FileService fileService;

    public List<HotelSearchResultDTO> search(HotelSearchDTO dto) {

        List<Hotel> hotels;

        if (dto.getHotelId() != null) {
            hotels = hotelRepository.findHotelAndNearby(dto.getHotelId());
        } else if (dto.getCityId() != null) {
            hotels = hotelRepository.findByCityId(dto.getCityId());
        } else if (dto.getRegionId() != null) {
            hotels = hotelRepository.findByRegionId(dto.getRegionId());
        } else if (dto.getCountryId() != null) {
            hotels = hotelRepository.findByCountryId(dto.getCountryId());
        } else {
            hotels = hotelRepository.searchByKeyword(dto.getKeyword() == null ? "" : dto.getKeyword().trim());
        }

        return hotels.stream().map(hotel -> mapToDTO(hotel, dto)).toList();
    }

    private HotelSearchResultDTO mapToDTO(Hotel hotel, HotelSearchDTO dto) {

        HotelSearchResultDTO r = new HotelSearchResultDTO();

        r.setHotelId(hotel.getId());
        r.setHotelName(hotel.getName());
        r.setRating(hotel.getRating());
        r.setDescription(hotel.getDescription());

        r.setCity(hotel.getAddress().getCity().getName());
        r.setRegion(hotel.getAddress().getCity().getRegion().getName());
        r.setCountry(hotel.getAddress().getCity().getRegion().getCountry().getName());

        r.setAddress(hotel.getAddress().getRoad());

        hotelAttachmentRepository.findTopByHotelIdAndHotelMediaTypeOrderByCreatedAtDesc(hotel.getId(),
                        HotelMediaType.PROFILE).ifPresent(att -> {
                            String url = fileService.getFileNames(
                            FileType.HOTEL_ATTACHMENT,
                            att.getId());
                    r.setImageUrl(url);});

        long nights = ChronoUnit.DAYS.between(dto.getCheckIn(), dto.getCheckOut());
        r.setNights((int) nights);
        r.setTotalPrice(pricingService.calculateTotalPrice(hotel.getId(), dto.getCheckIn(), dto.getCheckOut()));

        return r;
    }

}
