package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.home.HomeDTO;
import com.project.HotelManagementSystem.entity.FileStorage;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.repository.FileStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class HomeService {
    @Autowired
    private FileService fileService;
    @Autowired
    private FileStorageRepository fileStorageRepository;

    private MultipartFile multipartFile;

    public HomeDTO upload(MultipartFile file){
        multipartFile = file;
        fileService.handleFileUpload(multipartFile,FileType.ADMIN,1L,"s3");
        HomeDTO homeDTO = new HomeDTO();
        homeDTO.setProfileUrl(fileService.getFileName(FileType.ADMIN, 1L));
        homeDTO.setContentType(multipartFile.getContentType());
        return homeDTO;
    }

    public HomeDTO getUrl(){
        HomeDTO homeDTO = new HomeDTO();
        if(multipartFile == null){
            homeDTO.setProfileUrl(fileService.getFileName(FileType.ADMIN, 1L));
            homeDTO.setContentType(multipartFile.getContentType());
        }
        return homeDTO;
    }

}
