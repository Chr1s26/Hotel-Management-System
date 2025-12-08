package com.project.HotelManagementSystem.service.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.nodes.Ingest;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.document.hotel.HotelSearchDocument;
import com.project.HotelManagementSystem.dto.document.hotel.HotelSearchResponse;
import com.project.HotelManagementSystem.dto.document.hotel.PagingInfo;
import com.project.HotelManagementSystem.dto.searchFilter.hotel.HotelSearchQuery;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.specification.HotelSpecification;
import com.project.HotelManagementSystem.repository.HotelRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
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

    public HotelSearchResponse elasticSearchByQuery(String hotelName, Integer page, Integer size) throws IOException {
        int from = page * size;
        SearchResponse<HotelSearchDocument> hotelSearchResponse = elasticsearchClient.search(
                s -> s.index(AppConstants.HOTEL_INDEX_NAME)
                        .from(from)
                        .size(size)
                        .query(q -> q.multiMatch(m -> m
                .fields("name^2", "description")
                .query(hotelName)))
                , HotelSearchDocument.class);
        HotelSearchResponse searchResponse = new HotelSearchResponse();
        PagingInfo pagingInfo = new PagingInfo();

        pagingInfo.setPageNumber(page);
        pagingInfo.setPageSize(size);

        List<HotelSearchDocument> hotelSearchDocumentList = hotelSearchResponse.hits().hits().stream().map(Hit::source).filter(Objects::nonNull).toList();
        searchResponse.setHotelSearchDocumentList(hotelSearchDocumentList);
        searchResponse.setPagingInfo(pagingInfo);
        return searchResponse;
    }
}
