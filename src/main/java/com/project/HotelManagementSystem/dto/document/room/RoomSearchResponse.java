package com.project.HotelManagementSystem.dto.document.room;

import com.project.HotelManagementSystem.dto.document.PagingInfo;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class RoomSearchResponse {
    private List<RoomSearchDocument> roomSearchDocumentList;
    private PagingInfo pagingInfo;
}
