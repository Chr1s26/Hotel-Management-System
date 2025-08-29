package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class OtpController {
    @Autowired
    private UserRepository appUserRepository;

    @GetMapping("/confirm-account/otp")
    public String showConfirmAccountOtp(@RequestParam("email") String email, @RequestParam("message") String message , Model model){
        model.addAttribute("email",email);
        model.addAttribute("message",message);
        return "confirm-account/otp";
    }

    @PostMapping("/confirm-account/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp,@RequestParam String email){
        if(otp.equals("123")){
            User appUser = appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            appUser.setConfirmedAt(LocalDateTime.now());
            appUserRepository.save(appUser);
            return "redirect:/login";
        }
        return "redirect:/confirm-account/otp";
    }
}
