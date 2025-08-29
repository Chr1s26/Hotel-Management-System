package com.project.HotelManagementSystem.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.HotelManagementSystem.entity.FileStorage;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.FileStorageRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    public String getFileName(FileType fileType,Long fileId) {
        List<FileStorage> fileStorageList = fileStorageRepository.findByFileTypeAndFileId(fileType, fileId);
        if (fileStorageList.isEmpty()) {
            return null;
        }
        FileStorage fileStorage = fileStorageList.get(0);
        return getFileUrl(fileStorageList.get(0).getKey(),fileStorage.getServiceName());
    }

    public String getFileUrl(String fileKey,String serviceName){
        if("local".equals(serviceName)){
            return "/files/"+fileKey;
        }else{
            return String.format(s3BaseUrl,s3BucketName,region,fileKey);
        }
    }

    public void handleFileUpload(MultipartFile file, FileType fileType,Long id,String serviceName) {
        if (file.isEmpty()) {
            throw new ResourceNotFoundException("MultipartFile","file not found","file");
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

            FileStorage fileStorage = new FileStorage();
            fileStorage.setFileName(file.getOriginalFilename());
            fileStorage.setKey(storedFileName);
            fileStorage.setFileSize(file.getSize());
            fileStorage.setServiceName(serviceName);
            fileStorage.setFileType(fileType);
            fileStorage.setFileId(id);
            fileStorage.setContentType(file.getContentType());

            fileStorageRepository.save(fileStorage);
        } catch (IOException e) {
            throw new RuntimeException("Filed to upload file : "+e.getMessage());
        }
    }

}
