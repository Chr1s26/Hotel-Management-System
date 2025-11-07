package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.promotion.PromotionSearchQuery;
import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.PromotionSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionExportProcess extends CommonExportProcess<Promotion, PromotionSearchQuery> {

    @Autowired
    private PromotionSearchService promotionSearchService;

    public PromotionExportProcess(ExportListingService exportListingService, FileService fileService) {
        super(exportListingService, fileService);
    }

    @Override
    public String getRecordType() {
        return Promotion.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Promotion_Listing;
    }

    @Override
    public String getSheetName() {
        return "Promotions";
    }

    @Override
    public List<Promotion> fetchData(PromotionSearchQuery query) {
        return promotionSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Promotion>> columns() {
        return List.of(
                new ColumnSpec<>("ID", c -> String.valueOf(c.getId()),null),
                new ColumnSpec<>("Promotion Code", c -> String.valueOf(c.getCode()),null),
                new ColumnSpec<>("Discount Type", c -> c.getDiscountType() != null ? c.getDiscountType().name() : "",null),
                new ColumnSpec<>("Discount Amount", c -> String.valueOf(c.getDiscountAmount()),null),
                new ColumnSpec<>("Start Date", c -> c.getStartDate() != null ? c.getStartDate().toString() : "", null),
                new ColumnSpec<>("End Date", c -> c.getEndDate() != null ? c.getEndDate().toString() : "", null),
                new ColumnSpec<>("Point Amount", c -> String.valueOf(c.getPointAmount()),null),
                new ColumnSpec<>("Usage limit", c -> String.valueOf(c.getUsageLimit()),null),
                new ColumnSpec<>("Times Used", c -> String.valueOf(c.getTimesUsed()),null),
                new ColumnSpec<>("Status", c -> c.getStatus() != null ? c.getStatus().name() : "", null),
                new ColumnSpec<>("Created at", c ->
                        c.getCreatedAt() != null ? c.getCreatedAt().toString() : "", null),
                new ColumnSpec<>("Updated at", c ->
                        c.getUpdatedAt() != null ? c.getUpdatedAt().toString() : "", null),
                new ColumnSpec<>("Created by", c ->
                        c.getCreatedBy() != null ? c.getCreatedBy().getName() : "", null),
                new ColumnSpec<>("Updated by", c ->
                        c.getUpdatedBy() != null ? c.getUpdatedBy().getName() : "", null)

        );
    }

    @Override
    public String getListingRoute() {
        return "/promotions";
    }
}
