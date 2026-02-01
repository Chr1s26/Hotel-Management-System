package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.booking.HotelSearchDTO;
import com.project.HotelManagementSystem.dto.booking.HotelSearchResultDTO;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.entity.constants.HotelMediaType;
import com.project.HotelManagementSystem.exception.InvalidSearchException;
import com.project.HotelManagementSystem.repository.HotelAttachmentRepository;
import com.project.HotelManagementSystem.repository.HotelRepository;
import com.project.HotelManagementSystem.service.search.HotelSearchElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        validateDates(dto);

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

        if (dto.getHotelTypes() != null && !dto.getHotelTypes().isEmpty()) {
            hotels = hotels.stream()
                    .filter(h -> dto.getHotelTypes().contains(h.getHotelType()))
                    .toList();
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


    private void validateDates(HotelSearchDTO dto) {

        if (dto.getCheckIn() == null || dto.getCheckOut() == null) {
            throw new InvalidSearchException("Please select both check-in and check-out dates.");
        }

        LocalDate today = LocalDate.now();

        if (dto.getCheckIn().isBefore(today)) {
            throw new InvalidSearchException("Check-in date cannot be in the past.");
        }

        if (!dto.getCheckOut().isAfter(dto.getCheckIn())) {
            throw new InvalidSearchException("Check-out date must be after check-in date.");
        }
    }


}
