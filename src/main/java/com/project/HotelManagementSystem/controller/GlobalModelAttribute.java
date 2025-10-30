package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.service.AuthService;
import com.project.HotelManagementSystem.service.FileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttribute {
    private final AuthService authService;
    private final FileService fileService;
    private final ModelMapper modelMapper;

    @ModelAttribute("navbarProfileUrl")
    public String addProfileUrlToSideBar() {
        try {
            User user = authService.getCurrentUser();
            if (user == null) return "/images/default-profile.png";

            String url = fileService.getFileName(FileType.USER, user.getId());
            return (url != null && !url.isBlank()) ? url : "/images/default-profile.png";
        } catch (Exception e) {
            return "/images/default-profile.png";
        }
    }

    @ModelAttribute("navbarProfileName")
    public String addProfileNameToSideBar() {
        try {
            User user = authService.getCurrentUser();
            if (user == null) return "User";

            String name = user.getName();
            return (name != null && !name.isBlank()) ? name : "User";
        } catch (Exception e) {
            return "User";
        }
    }
}

