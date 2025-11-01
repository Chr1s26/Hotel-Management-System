package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.entity.ExportListing;
import com.project.HotelManagementSystem.repository.ExportListingRepository;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping
    public String listExports(Model model) {
        List<ExportListing> exports = exportListingService.getAllExports();
        model.addAttribute("exports", exports);
        return "exports/listing";
    }

    @GetMapping("/download-zip/{exportId}")
    public String generateZipFile(@PathVariable Long exportId, HttpServletResponse response) throws IOException {
        Optional<ExportListing> listing = exportListingRepository.findById(exportId);
        if (listing.isPresent()) {
            fileService.downloadExportFile(listing.get(), response, true);
        }
        return "redirect:/exports/";
    }

    @GetMapping("/delete/{id}")
    @ActiveRole("ADMIN")
    public String deleteFile(@PathVariable Long id) {
        this.exportListingService.deleteExportListing(id);
        return "redirect:/exports";
    }
}
