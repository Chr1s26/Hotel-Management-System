package com.project.HotelManagementSystem.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.project.HotelManagementSystem.dto.hotel.HotelPhotoDTO;
import com.project.HotelManagementSystem.entity.ExportListing;
import com.project.HotelManagementSystem.entity.FileStorage;
import com.project.HotelManagementSystem.entity.HotelAttachment;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.entity.constants.HotelMediaType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.ExportListingRepository;
import com.project.HotelManagementSystem.repository.FileStorageRepository;
import com.project.HotelManagementSystem.repository.HotelAttachmentRepository;
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
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

    private final FileStorageRepository fileStorageRepository;
    private final ExportListingRepository exportListingRepository;
    private final AmazonS3 amazonS3;
    @Autowired
    private AuthService authService;
    @Autowired
    private HotelAttachmentRepository hotelAttachmentRepository;

    public String getFileName(FileType fileType, Long fileId) {
        return fileStorageRepository
                .findTopByFileTypeAndFileIdOrderByCreatedAtDesc(fileType, fileId)
                .map(fs -> getFileUrl(fs.getKey(), fs.getServiceName()))
                .orElse("/images/default-profile.png");
    }

//    public String getFileName(FileType fileType,Long fileId) {
//        List<FileStorage> fileStorageList = fileStorageRepository.findByFileTypeAndFileId(fileType, fileId);
//        if (fileStorageList.isEmpty()) {
//            return "/images/default-profile.png";
//        }
//        FileStorage fileStorage = fileStorageList.get(0);
//        return getFileUrl(fileStorageList.get(0).getKey(),fileStorage.getServiceName());
//    }

    public String getFileUrl(String fileKey,String serviceName){
        if("local".equals(serviceName)){
            return "/files/"+fileKey;
        }else{
            return String.format(s3BaseUrl,s3BucketName,region,fileKey);
        }
    }

    public void handleFileUpload(MultipartFile file, FileType fileType,Long id,String serviceName) {
//        if (file.isEmpty()) {
//            throw new ResourceNotFoundException("MultipartFile","file not found","file");
//        }

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

            User user  = authService.getCurrentUser();

            FileStorage fileStorage = new FileStorage();
            fileStorage.setFileName(file.getOriginalFilename());
            fileStorage.setKey(storedFileName);
            fileStorage.setFileSize(file.getSize());
            fileStorage.setServiceName(serviceName);
            fileStorage.setFileType(fileType);
            fileStorage.setFileId(id);
            fileStorage.setContentType(file.getContentType());
            fileStorage.setCreatedAt(LocalDateTime.now());
            fileStorage.setCreatedBy(user);
            fileStorage.setUpdatedBy(user);
            fileStorage.setStatus(StatusType.ACTIVE);
            FileStorage fileStorage1 = fileStorageRepository.save(fileStorage);
        } catch (IOException e) {
            throw new RuntimeException("Filed to upload file : "+e.getMessage());
        }
    }

    public FileStorage saveExportFileWithStatus(ByteArrayInputStream inputStream, ExportListing exportListing) throws IOException {

        byte[] bytes = inputStream.readAllBytes();
        ByteArrayInputStream uploadStream = new ByteArrayInputStream(bytes);
        long fileSize = bytes.length;

        User user = authService.getCurrentUser();
        String fileName = exportListing.getFileName();

        String uuid = UUID.randomUUID().toString();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String s3Key = uuid + extension;

        FileStorage fileStorage = new FileStorage();

        fileStorage.setFileName(fileName);
        fileStorage.setFileSize(fileSize);
        fileStorage.setKey(s3Key);
        fileStorage.setServiceName("s3");
        fileStorage.setFileType(exportListing.getFileType());
        fileStorage.setFileId(exportListing.getId());
        fileStorage.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        fileStorage.setStatus(StatusType.COMPLETED);
        fileStorage.setCreatedAt(LocalDateTime.now());
        fileStorage.setCreatedBy(user);

        fileStorage = fileStorageRepository.save(fileStorage);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        metadata.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        amazonS3.putObject("hotel-export-report-bucket", s3Key, uploadStream, metadata);
        log.info("Export file uploaded successfully to S3: {}", s3Key);

        return fileStorage;
    }

    public void downloadExportFile(ExportListing exportListing, HttpServletResponse response, boolean asZip) throws IOException {

        FileStorage file = fileStorageRepository.findTopByFileIdAndFileTypeOrderByCreatedAtDesc(exportListing.getId(), exportListing.getFileType());
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


    public List<String> getFileNames(FileType fileType, Long fileId) {
        List<FileStorage> files = fileStorageRepository.findAllByFileTypeAndFileIdOrderByCreatedAtDesc(fileType,fileId);
        if(files.isEmpty()){
            return List.of("/images/default-profile.png");
        }
        return files.stream().map(fs -> getFileUrl(fs.getKey(), fs.getServiceName())).toList();
    }

    public List<String> getHotelFileNames(Long hotelId, HotelMediaType hotelMediaType) {
        List<HotelAttachment> attachments = hotelAttachmentRepository.findByHotelIdAndHotelMediaType(hotelId, hotelMediaType);

        List<String> urls = new ArrayList<>();
        for (HotelAttachment attachment : attachments) {
            List<String> fileUrls = getFileNames(FileType.HOTEL_ATTACHMENT, attachment.getId());
            urls.addAll(fileUrls);
        }
        return urls;
    }

    public List<HotelPhotoDTO> getHotelPhotos(Long hotelId, HotelMediaType hotelMediaType) {
        List<HotelAttachment> hotelAttachments = hotelAttachmentRepository.findByHotelIdAndHotelMediaType(hotelId, hotelMediaType);
        List<HotelPhotoDTO> hotelPhotos = new ArrayList<>();
        for(HotelAttachment attachment : hotelAttachments){
            List<String> fileUrls = getFileNames(FileType.HOTEL_ATTACHMENT, attachment.getId());
            for(String url : fileUrls){
                hotelPhotos.add(new HotelPhotoDTO(attachment.getId(), url));
            }
        }
        return hotelPhotos;
    }

    public void deleteHotelAttachmentAndFiles(Long attachmentId) {
        List<FileStorage> files = fileStorageRepository.findAllByFileTypeAndFileIdOrderByCreatedAtDesc(FileType.HOTEL_ATTACHMENT, attachmentId);
        for(FileStorage file : files) {
            try{
                if("S3".equalsIgnoreCase(file.getServiceName())){
                    amazonS3.deleteObject(new DeleteObjectRequest(s3BucketName, file.getKey()));
                }
                fileStorageRepository.delete(file);
            }catch (Exception e){
                throw new RuntimeException("Failed to delete hotel file: " + e.getMessage());
            }
        }
        hotelAttachmentRepository.deleteById(attachmentId);
    }

    public void saveExportFileWithFailStatus(ExportListing exportListing) {
        exportListing.setStatus(StatusType.FAIL);
        exportListingRepository.save(exportListing);
    }
}
