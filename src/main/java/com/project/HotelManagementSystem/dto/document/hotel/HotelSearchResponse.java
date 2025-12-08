package com.project.HotelManagementSystem.dto.document.hotel;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class HotelSearchResponse {
    private List<HotelSearchDocument> hotelSearchDocumentList;
    private PagingInfo pagingInfo;
}
