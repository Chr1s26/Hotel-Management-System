package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.dto.home.HomeDTO;
import com.project.HotelManagementSystem.dto.home.HomeRequestDTO;
import com.project.HotelManagementSystem.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomePageController {
    @Autowired
    private HomeService homeService;

    @GetMapping
    public String getHomePage(Model model) {
        HomeDTO homeDTO = homeService.getUrl();
        if(homeDTO != null) {
            model.addAttribute("url", homeDTO.getProfileUrl());
            model.addAttribute("type", homeDTO.getContentType());
            model.addAttribute("home", new HomeRequestDTO());
        }else{
            model.addAttribute("url", null);
            model.addAttribute("type", null);
            model.addAttribute("home", new HomeRequestDTO());
        }
        return "homePage/home";
    }

    @PostMapping("/upload")
    @ActiveRole("ADMIN")
    public String uploadWallpaper(@ModelAttribute("home") HomeRequestDTO requestDTO, Model model) {
        homeService.upload(requestDTO.getFile());
        return "redirect:/home";
    }
}
