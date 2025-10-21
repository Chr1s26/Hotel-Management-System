package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.Admin;
import com.project.HotelManagementSystem.entity.Editor;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.service.AbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class AuthController {
    private final AbstractService abstractService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/registerUser")
    public String showLoginForm(@ModelAttribute User appUser, Model model) {
        try{
            abstractService.registerNewUser(appUser);
        }catch(Exception e){
            model.addAttribute("registrationError",e.getMessage());
            return "register";
        }
        return "redirect:/login?registered";
    }

}
