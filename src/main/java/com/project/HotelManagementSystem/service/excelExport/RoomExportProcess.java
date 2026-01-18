package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.room.RoomSearchQuery;
import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.RoomSearchService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomExportProcess extends CommonExportProcess<Room, RoomSearchQuery> {
    private final RoomSearchService roomSearchService;

    public RoomExportProcess(ExportListingService exportListingService, FileService fileService, RoomSearchService roomSearchService, UserRepository userRepository) {
        super(exportListingService, fileService, userRepository);
        this.roomSearchService = roomSearchService;
    }

    @Override
    public String getRecordType() {
        return Room.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Room_Listing;
    }

    @Override
    public String getSheetName() {
        return "Rooms";
    }

    @Override
    public List<Room> fetchData(RoomSearchQuery query) {
        return roomSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Room>> columns() {
        return List.of(
                new ColumnSpec<>("ID",r -> String.valueOf(r.getId()), null),
                new ColumnSpec<>("Price", r -> String.valueOf(r.getRoomType().getPrice()),null),
                new ColumnSpec<>("Is Available", r -> String.valueOf(r.isAvailable()),null),
                new ColumnSpec<>("Description", Room::getDescription,null),
                new ColumnSpec<>("Room Type", r -> String.valueOf(r.getRoomType().getName()), null),
                new ColumnSpec<>("Max Capacity", r -> String.valueOf(r.getRoomType().getCapacity()), null),
                new ColumnSpec<>("Room Size", r -> String.valueOf(r.getRoomType().getRoomSize()), null),
                new ColumnSpec<>("Hotel", r -> r.getHotel()!=null ? r.getHotel().getName() : null, null),
                new ColumnSpec<>("Amenities", r -> r.getAmenities().stream().map(Amenities::getName).collect(Collectors.joining(", ")),null),
                new ColumnSpec<>("Promotions",r -> r.getPromotions().stream().map(Promotion::getCode).collect(Collectors.joining(", ")), null),
                new ColumnSpec<>("Status", r -> r.getStatus() != null ? r.getStatus().name() : "", null),
                new ColumnSpec<>("Created at", r -> r.getCreatedAt() != null ? r.getCreatedAt().toString() : "", null),
                new ColumnSpec<>("Updated at", r -> r.getUpdatedAt() != null ? r.getUpdatedAt().toString() : "", null),
                new ColumnSpec<>("Created by", r -> r.getCreatedBy() != null ? r.getCreatedBy().getName() : "", null),
                new ColumnSpec<>("Updated by", r -> r.getUpdatedBy() != null ? r.getUpdatedBy().getName() : "", null)
        );
    }

    @Override
    public String getListingRoute() {
        return "/rooms";
    }
}
