package com.project.HotelManagementSystem.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.project.HotelManagementSystem.entity.ExportListing;
import com.project.HotelManagementSystem.entity.FileStorageV2;
import com.project.HotelManagementSystem.entity.constants.Bucket;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.ExportListingRepository;
import com.project.HotelManagementSystem.repository.document.FileStorageV2Repository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class ExportListingService {
    private final ExportListingRepository exportListingRepository;
    private final AuthService authService;
    private final FileStorageV2Repository fileStorageV2Repository;
    private final AmazonS3 amazonS3;
    private final EmailService emailService;

    public ExportListingService(ExportListingRepository exportListingRepository, AuthService authService, FileStorageV2Repository fileStorageV2Repository, AmazonS3 amazonS3, EmailService emailService) {
        this.exportListingRepository = exportListingRepository;
        this.authService = authService;
        this.fileStorageV2Repository = fileStorageV2Repository;
        this.amazonS3 = amazonS3;
        this.emailService = emailService;
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
        Optional<ExportListing> exportListingOp = exportListingRepository.findById(id);

        if (!exportListingOp.isPresent()) {
             throw new ResourceNotFoundException("export",exportListingOp.get(),"id","/exports","Export listing not found");
        }
        ExportListing exportListing = exportListingOp.get();

        FileStorageV2 file = fileStorageV2Repository.findByFileTypeAndFileId(
                exportListing.getFileType(), exportListing.getId()
        ).orElse(null);

        if (file != null) {
            try {
                amazonS3.deleteObject(new DeleteObjectRequest(Bucket.EXPORT_BUCKET_NAME, file.getKey()));
                log.info("Deleted file from S3: {}", file.getKey());
                fileStorageV2Repository.delete(file);
            } catch (Exception e) {
                log.error("Failed to delete file from S3: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to delete file from S3: " + e.getMessage());
            }
        } else {
            log.warn("No file found for export listing ID {}", id);
        }
        exportListingRepository.delete(exportListing);
        log.info("Deleted ExportListing with ID {}", id);
    }

    public void sendExportFileByEmail(Long fileId, String recipient, String subject) throws IOException, MessagingException {
        ExportListing exportListing = exportListingRepository.findById(fileId).orElseThrow(() -> new IllegalArgumentException("File not found: "+ fileId));
        byte[] zipBytes = loadZipBytes(exportListing);
        String zipFileName = buildZipFileName(exportListing.getFileName());
        emailService.sendMailWithAttachment(recipient, subject, zipBytes, zipFileName);
    }

    private byte[] loadZipBytes(ExportListing exportListing) throws IOException {
        FileStorageV2 file = fileStorageV2Repository.findByFileTypeAndFileId(exportListing.getFileType(), exportListing.getId()).orElse(null);
        if(file == null){
            throw new ResourceNotFoundException("FileStorage", file, "fileId", "/exports", "File not found");
        }
        S3Object s3Object = amazonS3.getObject(Bucket.EXPORT_BUCKET_NAME, file.getKey());
        try (S3ObjectInputStream s3is = s3Object.getObjectContent();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)){
            byte[] excelBytes = s3is.readAllBytes();
            String xlsxFileName = prepareFileName(file.getFileName());
            ZipEntry entry = new ZipEntry(xlsxFileName);
            zos.putNextEntry(entry);
            zos.write(excelBytes);
            zos.closeEntry();
            zos.finish();
            return baos.toByteArray();
        }
    }

    private String prepareFileName(String originalFileName) {
        if(originalFileName == null || originalFileName.isBlank()){
            return  "export.xlsx";
        }
        return originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

    private String buildZipFileName(String originalFileName){
        String safe = prepareFileName(originalFileName);
        if(safe.toLowerCase().endsWith(".xlsx")){
            return safe.replaceAll("(?i)\\.xlsx$", ".zip");
        }
        return safe+".zip";
    }

}
