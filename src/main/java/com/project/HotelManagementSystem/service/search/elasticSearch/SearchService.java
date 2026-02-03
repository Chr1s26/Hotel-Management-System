package com.project.HotelManagementSystem.service.search.elasticSearch;

import com.project.HotelManagementSystem.dto.booking.HotelSearchDTO;
import com.project.HotelManagementSystem.dto.booking.HotelSearchResultDTO;
import com.project.HotelManagementSystem.dto.booking.PricingResult;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.exception.InvalidSearchException;
import com.project.HotelManagementSystem.repository.HotelAttachmentRepository;
import com.project.HotelManagementSystem.repository.HotelRepository;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.PricingService;
import com.project.HotelManagementSystem.service.cache.CacheKeyBuilder;
import com.project.HotelManagementSystem.service.cache.HotelSearchCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final HotelSearchElasticService elasticService;
    private final HotelRepository hotelRepository;
    private final HotelAttachmentRepository hotelAttachmentRepository;
    private final FileService fileService;
    private final PricingService pricingService;
    private final HotelSearchCacheService cacheService;

    public List<HotelSearchResultDTO> search(HotelSearchDTO dto) throws IOException {

        validateDates(dto);

        String cacheKey = CacheKeyBuilder.build(dto);
        List<HotelSearchResultDTO> cached = cacheService.get(cacheKey);
        if (cached != null) return cached;

        List<Long> hotelIds = elasticService.search(dto);
        if (hotelIds.isEmpty()) return List.of();

        List<Hotel> hotels = hotelRepository.findAllById(hotelIds);

        List<HotelSearchResultDTO> results = hotels.stream()
                .map(h -> map(h, dto))
                .toList();

        cacheService.set(cacheKey, results);
        return results;
    }

    private HotelSearchResultDTO map(Hotel hotel, HotelSearchDTO dto) {

        HotelSearchResultDTO r = new HotelSearchResultDTO();

        r.setHotelId(hotel.getId());
        r.setHotelName(hotel.getName());
        r.setRating(hotel.getRating());
        r.setReviewCount(hotel.getReviews().size());
        r.setDescription(hotel.getDescription());

        r.setCity(hotel.getAddress().getCity().getName());
        r.setRegion(hotel.getAddress().getCity().getRegion().getName());
        r.setCountry(hotel.getAddress().getCity().getRegion().getCountry().getName());

        long nights = ChronoUnit.DAYS.between(dto.getCheckIn(), dto.getCheckOut());
        r.setNights((int) nights);

        PricingResult pricing = pricingService.calculateHotelPrice(hotel, dto.getCheckIn(), dto.getCheckOut(), dto.getNumberOfGuests());

        r.setOriginalPrice(pricing.getOriginalPrice());
        r.setDiscountedPrice(pricing.getDiscountedPrice());
        r.setDiscountLabel(pricing.getDiscountLabel());

        List<String> images = hotelAttachmentRepository.findTop5ByHotel_IdOrderByCreatedAtDesc(hotel.getId()).stream()
                .map(att -> fileService.getFileNames(
                        FileType.HOTEL_ATTACHMENT,
                        att.getId()))
                .toList();

        r.setImageUrls(images);

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
