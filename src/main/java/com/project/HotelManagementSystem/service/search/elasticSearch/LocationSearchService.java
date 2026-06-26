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

//
//curl -X PUT "http://localhost:9200/hotel_search" \
//        -H "Content-Type: application/json" \
//        -d '{
//        "settings": {
//        "analysis": {
//        "analyzer": {
//        "autocomplete": {
//        "tokenizer": "edge_ngram_tokenizer",
//        "filter": ["lowercase"]
//        }
//        },
//        "tokenizer": {
//        "edge_ngram_tokenizer": {
//        "type": "edge_ngram",
//        "min_gram": 1,
//        "max_gram": 20,
//        "token_chars": ["letter"]
//        }
//        }
//        }
//        },
//        "mappings": {
//        "properties": {
//        "hotelId": { "type": "long" },
//
//        "hotelName": {
//        "type": "text",
//        "analyzer": "autocomplete",
//        "search_analyzer": "standard"
//        },
//
//        "city": {
//        "type": "text",
//        "analyzer": "autocomplete",
//        "search_analyzer": "standard"
//        },
//        "region": {
//        "type": "text",
//        "analyzer": "autocomplete",
//        "search_analyzer": "standard"
//        },
//        "country": {
//        "type": "text",
//        "analyzer": "autocomplete",
//        "search_analyzer": "standard"
//        },
//
//        "hotelType": { "type": "keyword" },
//
//        "minRoomPrice": { "type": "double" },
//        "maxRoomPrice": { "type": "double" },
//
//        "reviewAvg": { "type": "double" },
//        "reviewCount": { "type": "integer" },
//
//        "roomTypeNames": { "type": "keyword" },
//        "amenities": { "type": "keyword" },
//
//        "hasPromotion": { "type": "boolean" }
//        }
//        }
//        }'