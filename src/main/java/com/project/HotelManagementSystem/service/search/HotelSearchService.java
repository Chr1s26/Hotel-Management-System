package com.project.HotelManagementSystem.service.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.document.hotel.HotelSearchDocument;
import com.project.HotelManagementSystem.dto.searchFilter.hotel.HotelSearchQuery;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.specification.HotelSpecification;
import com.project.HotelManagementSystem.repository.HotelRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class HotelSearchService {

    private final CommonSearchService commonSearchService;
    private final HotelRepository hotelRepository;
    private final ElasticsearchClient elasticsearchClient;

    public Page<Hotel> searchByQuery(HotelSearchQuery query) {
        return commonSearchService.searchByQuery(hotelRepository, HotelSpecification::fromFilter, query);
    }

    public List<Hotel> searchByQueryAll(HotelSearchQuery query) {
        return commonSearchService.searchByQueryAll(hotelRepository, HotelSpecification::fromFilter,query);
    }

    public List<HotelSearchDocument> elasticSearchByQuery(String hotelName) throws IOException {
        SearchResponse<HotelSearchDocument> hotelSearchResponse = elasticsearchClient.search(s -> s.index(AppConstants.HOTEL_INDEX_NAME).query(q -> q.multiMatch(m -> m
                .fields("name^2", "description")
                .query(hotelName)))
                , HotelSearchDocument.class);
        return hotelSearchResponse.hits().hits().stream().map(Hit::source).filter(Objects::nonNull).toList();
    }
}
