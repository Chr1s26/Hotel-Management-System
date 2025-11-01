package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.ExportListing;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.ExportListingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ExportListingService {
    private final ExportListingRepository exportListingRepository;
    private final AuthService authService;

    public ExportListingService(ExportListingRepository exportListingRepository, AuthService authService) {
        this.exportListingRepository = exportListingRepository;
        this.authService = authService;
    }

    public ExportListing saveExportListing(String entityName, FileType fileType) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String fileName = entityName + "-List-" + timestamp + ".xlsx";

        ExportListing listing = new ExportListing();
        listing.setFileName(fileName);
        listing.setFileType(fileType);
        listing.setCreatedAt(LocalDateTime.now());
        listing.setCreatedBy(authService.getCurrentUser());
        listing.setStatus(StatusType.COMPLETED);
        return exportListingRepository.save(listing);
    }

    public List<ExportListing> getAllExports() {
        return exportListingRepository.findAll();
    }

    public void deleteExportListing(Long id) {
        Optional<ExportListing> exportListing = exportListingRepository.findById(id);
        if (exportListing.isEmpty()) {
            throw new ResourceNotFoundException("exportListing",exportListing,"id","exports","File with this id cannot be found");
        }
        exportListingRepository.deleteById(id);
    }
}
