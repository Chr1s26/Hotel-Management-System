package com.project.HotelManagementSystem.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.project.HotelManagementSystem.entity.ExportListing;
import com.project.HotelManagementSystem.entity.FileStorageV2;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.FileNotUploadException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.ExportListingRepository;
import com.project.HotelManagementSystem.repository.HotelAttachmentRepository;
import com.project.HotelManagementSystem.repository.document.FileStorageV2Repository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Data
@RequiredArgsConstructor
@Slf4j
@Service
public class FileService {
    public static final String UPLOAD_DIRECTORY = "/Users/chr1skak/Documents/hotel_file_import";

    @Value("${cloud.aws.bucket}")
    private String s3BucketName;

    private String s3BaseUrl = "https://%s.s3.%s.amazonaws.com/%s";

    @Value("${cloud.aws.region.static}")
    private String region;

    private final FileStorageV2Repository fileStorageV2Repository;
    private final ExportListingRepository exportListingRepository;
    private final AmazonS3 amazonS3;
    @Autowired
    private AuthService authService;
    @Autowired
    private HotelAttachmentRepository hotelAttachmentRepository;

    public String getFileName(FileType fileType, Long fileId) {
        return fileStorageV2Repository
                .findByFileTypeAndFileId(fileType, fileId)
                .map(fs -> getFileUrl(fs.getKey(), fs.getServiceName()))
                .orElse("/images/default-profile.png");
    }

    public String getFileUrl(String fileKey,String serviceName){
        if("local".equals(serviceName)){
            return "/files/"+fileKey;
        }else{
            return String.format(s3BaseUrl,s3BucketName,region,fileKey);
        }
    }


    public void handleFileUpload(MultipartFile file, FileType fileType,Long id,String serviceName) {

        if (file == null || file.isEmpty()) {
            throw new FileNotUploadException("Profile picture not found");
        }

        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String storedFileName = uuid + fileExtension;

        try{
            if("local".equalsIgnoreCase(serviceName)){
                File dir = new File(UPLOAD_DIRECTORY);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String filePath = UPLOAD_DIRECTORY + File.separator + storedFileName;
                file.transferTo(new File(filePath));
            }else{
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(file.getContentType());
                objectMetadata.setContentLength(file.getSize());
                amazonS3.putObject(s3BucketName, storedFileName, file.getInputStream(), objectMetadata);
                log.info("File Uploaded Successfully to S3 : {}",storedFileName);
            }

            FileStorageV2 fileStorage = new FileStorageV2();
            fileStorage.setFileName(file.getOriginalFilename());
            fileStorage.setKey(storedFileName);
            fileStorage.setFileSize(file.getSize());
            fileStorage.setServiceName(serviceName);
            fileStorage.setFileType(fileType);
            fileStorage.setFileId(id);
            fileStorage.setContentType(file.getContentType());
            fileStorageV2Repository.save(fileStorage);

        } catch (IOException e) {
            throw new RuntimeException("Filed to upload file : "+e.getMessage());
        }
    }

    public FileStorageV2 saveExportFileWithStatus(ByteArrayInputStream inputStream, ExportListing exportListing) throws IOException {

        byte[] bytes = inputStream.readAllBytes();
        ByteArrayInputStream uploadStream = new ByteArrayInputStream(bytes);
        long fileSize = bytes.length;

        User user = authService.getCurrentUser();
        String fileName = exportListing.getFileName();

        String uuid = UUID.randomUUID().toString();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String s3Key = uuid + extension;

        FileStorageV2 fileStorage = new FileStorageV2();

        fileStorage.setFileName(fileName);
        fileStorage.setFileSize(fileSize);
        fileStorage.setKey(s3Key);
        fileStorage.setServiceName("s3");
        fileStorage.setFileType(exportListing.getFileType());
        fileStorage.setFileId(exportListing.getId());
        fileStorage.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        fileStorage = fileStorageV2Repository.save(fileStorage);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        metadata.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        amazonS3.putObject("hotel-export-report-bucket", s3Key, uploadStream, metadata);
        log.info("Export file uploaded successfully to S3: {}", s3Key);

        return fileStorage;
    }

    public void downloadExportFile(ExportListing exportListing, HttpServletResponse response, boolean asZip) throws IOException {
        FileStorageV2 file = fileStorageV2Repository.findByFileTypeAndFileId(exportListing.getFileType(), exportListing.getId()).orElse(null);
        if (file == null) {
            throw new ResourceNotFoundException("FileStorage", file, "fileId", "/exports","File not found");
        }
        S3Object s3Object = amazonS3.getObject("hotel-export-report-bucket", file.getKey());
        S3ObjectInputStream s3is = s3Object.getObjectContent();

        if (asZip) {
            // Deliver as ZIP
            response.setContentType("application/zip");
            String zipFileName = exportListing.getFileName().replaceAll("[^a-zA-Z0-9\\.\\-]", "_").replace(".xlsx", ".zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + "\"");

            // Read Excel bytes fully
            byte[] excelBytes = s3is.readAllBytes();

            // Create ZIP in memory
            try (ServletOutputStream sos = response.getOutputStream();
                 ZipOutputStream zos = new ZipOutputStream(sos)) {

                ZipEntry entry = new ZipEntry(exportListing.getFileName().replaceAll("[^a-zA-Z0-9\\.\\-]", "_"));
                zos.putNextEntry(entry);
                zos.write(excelBytes);
                zos.closeEntry();
                zos.finish();
            }
        } else {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String excelFileName = exportListing.getFileName().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + excelFileName + "\"");

            try (ServletOutputStream sos = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int len;
                while ((len = s3is.read(buffer)) != -1) {
                    sos.write(buffer, 0, len);
                }
            }
        }
    }


    public String getFileNames(FileType fileType, Long fileId) {
        List<FileStorageV2> files = fileStorageV2Repository.findAllByFileTypeAndFileIdOrderByIdDesc(fileType, fileId);
        if (files.isEmpty()) {
            return "/images/default-profile.png";
        }
        return getFileUrl(files.get(0).getKey(), files.get(0).getServiceName());
    }

    public void saveExportFileWithFailStatus(ExportListing exportListing) {
        exportListing.setStatus(StatusType.FAIL);
        exportListingRepository.save(exportListing);
    }

    public String getBucketName(){
        return s3BucketName;
    }

}
