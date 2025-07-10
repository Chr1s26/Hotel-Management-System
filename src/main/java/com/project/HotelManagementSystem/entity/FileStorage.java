package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.entity.constants.FileType;
import jakarta.persistence.*;
import lombok.Data;

@Table
@Data
@Entity
public class FileStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private FileType fileType;
    private Long fileId;
    private String fileName;
    private String key;
    private String contentType;
    private long fileSize;
    private String serviceName;
}
