package com.project.HotelManagementSystem.repository.document;

import com.project.HotelManagementSystem.entity.FileStorageV2;
import com.project.HotelManagementSystem.entity.constants.FileType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FileStorageV2Repository extends MongoRepository<FileStorageV2,String> {
    Optional<FileStorageV2> findByFileTypeAndFileId(FileType fileType, Long fileId);
    List<FileStorageV2> findAllByFileTypeAndFileIdOrderByIdDesc(FileType fileType, Long fileId);
}
