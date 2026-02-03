package com.project.HotelManagementSystem.service.search.elasticSearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.project.HotelManagementSystem.dto.booking.HotelSearchDocument;
import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.Review;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.repository.HotelRepository;
import com.project.HotelManagementSystem.service.PromotionCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelIndexService {

    private final ElasticsearchClient esClient;
    private final HotelRepository hotelRepository;
    private final PromotionCalculator promotionCalculator;

    public void reindexHotel(Long hotelId) throws IOException {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow();

        HotelSearchDocument doc = buildDocument(hotel);

        esClient.index(i -> i
                .index("hotel_search")
                .id(hotel.getId().toString())
                .document(doc)
        );
    }

    private HotelSearchDocument buildDocument(Hotel hotel) {

        List<Room> rooms = hotel.getRooms();

        double minPrice = rooms.stream()
                .mapToDouble(room -> promotionCalculator.applyPromotionPrice(
                        room.getRoomType().getPrice(),
                        room.getPromotions(),
                        hotel.getPromotions()
                ))
                .min()
                .orElse(0);

        double maxPrice = rooms.stream()
                .mapToDouble(room -> promotionCalculator.applyPromotionPrice(
                        room.getRoomType().getPrice(),
                        room.getPromotions(),
                        hotel.getPromotions()
                ))
                .max()
                .orElse(0);


        Set<String> roomTypes = rooms.stream()
                .map(r -> r.getRoomType().getName())
                .map(String::toUpperCase)
                .collect(Collectors.toSet());

        Set<String> amenities = rooms.stream()
                .flatMap(r -> r.getAmenities().stream())
                .map(a -> a.getName().toLowerCase())
                .collect(Collectors.toSet());

        boolean hasPromotion =
                !hotel.getPromotions().isEmpty() ||
                        rooms.stream().anyMatch(r -> !r.getPromotions().isEmpty());

        return new HotelSearchDocument(
                hotel.getId(),
                hotel.getName(),
                hotel.getAddress().getCity().getName(),
                hotel.getAddress().getCity().getRegion().getName(),
                hotel.getAddress().getCity().getRegion().getCountry().getName(),
                hotel.getHotelType().name(),
                minPrice,
                maxPrice,
                hotel.getReviews().stream().mapToDouble(Review::getRating).average().orElse(0),
                hotel.getReviews().size(),
                roomTypes,
                amenities,
                hasPromotion
        );
    }
}

