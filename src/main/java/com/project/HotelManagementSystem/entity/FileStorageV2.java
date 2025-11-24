package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.entity.constants.FileType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "file_storage")
@Data
public class FileStorageV2 {

    @Id
    private String id;
    private FileType fileType;
    private Long fileId;
    private String fileName;
    private String key;
    private String contentType;
    private long fileSize;
    private String serviceName;
}
