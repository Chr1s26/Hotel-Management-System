package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.entity.constants.FileType;
import jakarta.persistence.*;
import lombok.Data;

@Table
@Data
@Entity
public class FileStorage extends MasterData {

    @Column(nullable = false)
    //Jpeg
    private FileType fileType;
    @Column(nullable = false)
    private Long fileId;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String key;
    @Column(nullable = false)
    private String contentType;
    @Column(nullable = false)
    private long fileSize;
    @Column(nullable = false)
    private String serviceName;
}
