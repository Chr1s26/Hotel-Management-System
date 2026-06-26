package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.exception.OtpExpiredException;
import com.project.HotelManagementSystem.exception.OtpInvalidException;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OtpController {
    @Autowired
    private UserRepository appUserRepository;
    @Autowired
    private OtpService otpService;

    @GetMapping("/confirm-account/otp")
    public String showConfirmAccountOtp(@RequestParam("email") String email, @RequestParam("message") String message , Model model){
        model.addAttribute("email",email);
        model.addAttribute("message",message);
        User appUser = appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(!message.equals("OTP is Invalid")){
            otpService.sendOtp(appUser);
        }
        return "confirm-account/otp";
    }

    @PostMapping("/confirm-account/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp,@RequestParam String email){
        try{
            otpService.isOtpValid(email, otp);
            return "redirect:/login?otpMessage=true";
        }catch (OtpExpiredException e){
            String encodedMessage = "OTP is expired";
            return "redirect:/confirm-account/otp?email=" + email + "&error=unconfirmed&message=" + encodedMessage;
        }catch (OtpInvalidException e){
            String encodedMessage = "OTP is Invalid";
            return "redirect:/confirm-account/otp?email=" + email + "&error=unconfirmed&message=" + encodedMessage;
        }
    }

    @PostMapping("/confirm-account/resend-otp")
    public String resendOtp(@RequestParam String email){
        String encodedMessage = "OTP is resent";
        return "redirect:/confirm-account/otp?email=" + email + "&error=unconfirmed&message=" + encodedMessage;
    }
}
