package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.service.AdminService;
import com.project.HotelManagementSystem.service.AuthService;
import com.project.HotelManagementSystem.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class ProfileController {

    @Autowired
    private AuthService authService;
    @Autowired
    private ProfileService profileService;

    @PostMapping("/profiles")
    public String getProfile(Model model) {
        model.addAttribute("user",authService.getCurrentUser());
        model.addAttribute("userType",profileService.getProfile());
        return "profiles/userProfile";
    }

    @PostMapping("/upload")
    public String uploadProfile(MultipartFile file, Model model) {

    }
}
