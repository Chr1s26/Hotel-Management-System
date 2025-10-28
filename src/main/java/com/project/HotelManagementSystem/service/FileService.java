package com.project.HotelManagementSystem.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.HotelManagementSystem.entity.FileStorage;
import com.project.HotelManagementSystem.entity.HotelAttachment;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.entity.constants.HotelMediaType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.FileStorageRepository;
import com.project.HotelManagementSystem.repository.HotelAttachmentRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

}
