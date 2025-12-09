package com.project.HotelManagementSystem.service.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.document.PagingInfo;
import com.project.HotelManagementSystem.dto.document.hotel.HotelSearchDocument;
import com.project.HotelManagementSystem.dto.document.room.RoomSearchDocument;
import com.project.HotelManagementSystem.dto.document.room.RoomSearchResponse;
import com.project.HotelManagementSystem.dto.searchFilter.room.RoomSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.room.RoomSearchQuery;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.entity.specification.RoomSpecification;
import com.project.HotelManagementSystem.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RoomSearchService {

    private final CommonSearchService commonSearchService;
    private final RoomRepository roomRepository;
    private final ElasticsearchClient elasticsearchClient;

    public Page<Room> searchByQuery(RoomSearchQuery query) {
        return commonSearchService.searchByQuery(roomRepository, RoomSpecification::fromFilter,query);
    }

    public List<Room> searchByQueryAll(RoomSearchQuery query) {
        return commonSearchService.searchByQueryAll(roomRepository, RoomSpecification::fromFilter,query);
    }

    public RoomSearchResponse elasticSearchByQuery(RoomSearchQuery query) throws IOException {
        int page = query.getPageNumber();
        int size = query.getPageSize();
        int from = page * size;

        String searchValue = query.getFilterValue(RoomSearchField.PRICE);

        SearchResponse<RoomSearchDocument> searchResponse = elasticsearchClient.search(
                s -> s.index(AppConstants.ROOM_INDEX_NAME)
                        .from(from)
                        .size(size)
                        .query(q -> q.multiMatch(m -> m
                                .fields("price^2", "isAvailable","roomType","maxCapacity")
                                .query(searchValue)))
                , RoomSearchDocument.class);

        RoomSearchResponse response = new RoomSearchResponse();
        PagingInfo pagingInfo = new PagingInfo();

        pagingInfo.setPageNumber(page);
        pagingInfo.setPageSize(size);

        List<RoomSearchDocument> roomSearchDocumentList = searchResponse.hits().hits().stream().map(Hit::source).filter(Objects::nonNull).toList();
        response.setRoomSearchDocumentList(roomSearchDocumentList);
        response.setPagingInfo(pagingInfo);
        return response;
    }
}
