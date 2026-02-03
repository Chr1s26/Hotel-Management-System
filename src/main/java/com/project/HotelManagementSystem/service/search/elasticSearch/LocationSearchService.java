package com.project.HotelManagementSystem.service.search.elasticSearch;


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
                                        .should(s1 -> s1.match(m -> m
                                                .field("name")
                                                .query(keyword)
                                                .boost(5f)
                                        ))
                                        .should(s2 -> s2.match(m -> m
                                                .field("cityName")
                                                .query(keyword)
                                                .boost(4f)
                                        ))
                                        .should(s3 -> s3.match(m -> m
                                                .field("regionName")
                                                .query(keyword)
                                                .boost(3f)
                                        ))
                                        .should(s4 -> s4.match(m -> m
                                                .field("countryName")
                                                .query(keyword)
                                                .boost(2f)
                                        ))
                                )),
                        LocationSearchDocument.class
                );

        return res.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .toList();
    }

}
