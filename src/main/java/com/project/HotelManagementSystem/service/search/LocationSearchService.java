package com.project.HotelManagementSystem.service.search;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.booking.LocationSearchDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LocationSearchService {

    private final ElasticsearchClient client;

    public List<LocationSearchDocument> autocomplete(String keyword) throws Exception {

        SearchResponse<LocationSearchDocument> res =
                client.search(s -> s
                                .index(AppConstants.LOCATION_INDEX)
                                .size(10)
                                .query(q -> q.bool(b -> b
                                        .should(s1 -> s1.prefix(p ->
                                                p.field("name").value(keyword)
                                        ))
                                        .should(s2 -> s2.prefix(p ->
                                                p.field("regionName").value(keyword)
                                        ))
                                        .should(s3 -> s3.prefix(p ->
                                                p.field("countryName").value(keyword)
                                        ))
                                ))
                        , LocationSearchDocument.class);

        return res.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .toList();
    }
}
