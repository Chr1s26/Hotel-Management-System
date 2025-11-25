package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.exportListing.ExportListingSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.exportListing.ExportListingSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.exportListing.ExportListingSearchQuery;
import com.project.HotelManagementSystem.entity.ExportListing;
import com.project.HotelManagementSystem.repository.ExportListingRepository;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.ExportListingSearchService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/exports")
@RequiredArgsConstructor
public class ExportListingController {
    private final ExportListingService exportListingService;
    private final FileService fileService;
    private final ExportListingRepository exportListingRepository;
    private final ExportListingSearchService exportListingSearchService;

    @ModelAttribute("query")
    public ExportListingSearchQuery initQuery() {
        ExportListingSearchQuery query = new ExportListingSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new ExportListingSearchFilter(ExportListingSearchField.FILE_NAME, MatchType.CONTAINS,""),
                new ExportListingSearchFilter(ExportListingSearchField.FILE_TYPE, MatchType.EXACT, ""),
                new ExportListingSearchFilter(ExportListingSearchField.STATUS, MatchType.EXACT, "")
        ));
        return query;
    }

    @GetMapping
    public String listExports(Model model, @ModelAttribute("query") ExportListingSearchQuery query) {
        Page<ExportListing> page = exportListingSearchService.searchByQuery(query);
        model.addAttribute("exports", page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "exports/listing";
    }

    @PostMapping
    public String searchExports(Model model, @ModelAttribute("query") ExportListingSearchQuery query) {
        Page<ExportListing> page = exportListingSearchService.searchByQuery(query);
        model.addAttribute("exports", page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "exports/listing";
    }

    @GetMapping("/download-zip/{exportId}")
    public void generateZipFile(@PathVariable Long exportId, HttpServletResponse response) throws IOException {
        Optional<ExportListing> listing = exportListingRepository.findById(exportId);
        if (listing.isPresent()) {
            fileService.downloadExportFile(listing.get(), response, true);
        }else{
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Export not found");
        }
    }

    @PostMapping("/delete/{id}")
    @ActiveRole("ADMIN")
    public String deleteFile(@PathVariable Long id) {
        this.exportListingService.deleteExportListing(id);
        return "redirect:/exports";
    }

    @PostMapping("/exports/send")
    public String sendExport(){
        return "redirect:/exports";
    }
}
