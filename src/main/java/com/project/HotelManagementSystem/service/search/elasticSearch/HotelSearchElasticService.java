package com.project.HotelManagementSystem.service.search.elasticSearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import com.project.HotelManagementSystem.dto.booking.HotelSearchDTO;
import com.project.HotelManagementSystem.dto.booking.HotelSearchDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelSearchElasticService {

    private final ElasticsearchClient client;

    public List<Long> search(HotelSearchDTO dto) throws IOException {

        return client.search(s -> s
                                .index("hotel_search")
                                .query(q -> q.bool(b -> {

                                    if (dto.getKeyword() != null && !dto.getKeyword().isBlank()) {
                                        b.must(m -> m.bool(bb -> bb
                                                .should(s1 -> s1.match(mm -> mm
                                                        .field("hotelName")
                                                        .query(dto.getKeyword()).boost(4f)
                                                ))
                                                .should(s2 -> s2.match(mm -> mm
                                                        .field("city")
                                                        .query(dto.getKeyword()).boost(3f)
                                                ))
                                                .should(s3 -> s3.match(mm -> mm
                                                        .field("region")
                                                        .query(dto.getKeyword()).boost(2f)
                                                ))
                                                .should(s4 -> s4.match(mm -> mm
                                                        .field("country")
                                                        .query(dto.getKeyword()).boost(1f)
                                                ))
                                                .minimumShouldMatch("1")
                                        ));
                                    }

                                    if (dto.getHotelTypes() != null && !dto.getHotelTypes().isEmpty()) {
                                        b.filter(f -> f.terms(t ->
                                                t.field("hotelType")
                                                        .terms(v -> v.value(
                                                                dto.getHotelTypes().stream()
                                                                        .map(type -> FieldValue.of(type.name()))
                                                                        .toList()
                                                        ))
                                        ));
                                    }


                                    if (dto.getMinPrice() != null || dto.getMaxPrice() != null) {
                                        b.filter(f -> f.range(r -> r.number(n -> {
                                            n.field("minRoomPrice");
                                            if (dto.getMinPrice() != null) n.gte(dto.getMinPrice());
                                            if (dto.getMaxPrice() != null) n.lte(dto.getMaxPrice());
                                            return n;
                                        })));
                                    }

                                    if (dto.getMinRating() != null) {
                                        b.filter(f -> f.range(r ->
                                                r.number(n -> n
                                                        .field("reviewAvg")
                                                        .gte(dto.getMinRating())
                                                )
                                        ));
                                    }

                                    if (dto.getRoomTypeNames() != null && !dto.getRoomTypeNames().isEmpty()) {
                                        b.filter(f -> f.terms(t ->
                                                t.field("roomTypeNames")
                                                        .terms(v -> v.value(
                                                                dto.getRoomTypeNames().stream()
                                                                        .map(String::toUpperCase)
                                                                        .map(FieldValue::of)
                                                                        .toList()
                                                        ))
                                        ));
                                    }

                                    if (dto.getAmenities() != null && !dto.getAmenities().isEmpty()) {
                                        dto.getAmenities().forEach(a ->
                                                b.filter(f -> f.match(m ->
                                                        m.field("amenities")
                                                                .query(a.toLowerCase())
                                                ))
                                        );
                                    }

                                    if (dto.isPromotion()) {
                                        b.filter(f -> f.term(t ->
                                                t.field("hasPromotion")
                                                        .value(true)
                                        ));
                                    }

                                    return b;
                                }))
                        , HotelSearchDocument.class
                ).hits().hits()
                .stream()
                .map(h -> h.source().getHotelId())
                .toList();
    }


}

