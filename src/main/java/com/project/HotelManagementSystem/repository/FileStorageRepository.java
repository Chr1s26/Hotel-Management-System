package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.FileStorage;
import com.project.HotelManagementSystem.entity.constants.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
    List<FileStorage> findByFileTypeAndFileId(FileType fileType, Long fileId);
}
