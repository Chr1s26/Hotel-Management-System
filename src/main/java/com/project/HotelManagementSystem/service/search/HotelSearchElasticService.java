package com.project.HotelManagementSystem.service.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.document.hotel.HotelSearchDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelSearchElasticService {

    private final ElasticsearchClient client;

    public List<Long> searchHotelIds(String keyword) throws Exception {

        SearchResponse<HotelSearchDocument> response =
                client.search(s -> s
                                .index(AppConstants.HOTEL_INDEX_NAME)
                                .size(20)
                                .query(q -> q.multiMatch(m -> m
                                        .query(keyword)
                                        .fields("name^5", "city^3", "region^2", "country")
                                )),
                        HotelSearchDocument.class
                );

        return response.hits().hits().stream()
                .map(h -> Long.parseLong(h.source().getId()))
                .toList();
    }
}

