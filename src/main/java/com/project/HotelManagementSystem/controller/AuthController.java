package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.dto.forget_password.EmailForm;
import com.project.HotelManagementSystem.dto.forget_password.ResetPasswordDTO;
import com.project.HotelManagementSystem.dto.user.UserCreateDTO;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.exception.OtpExpiredException;
import com.project.HotelManagementSystem.exception.OtpInvalidException;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.service.AbstractService;
import com.project.HotelManagementSystem.service.AuthService;
import com.project.HotelManagementSystem.service.OtpService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class AuthController {
    private final AbstractService abstractService;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final OtpService otpService;

    @GetMapping("/register")
    public String showRegistrationForm(@RequestParam(value = "registration", required = false, defaultValue = "false") String registration, Model model) {
        model.addAttribute("user", new UserCreateDTO());
        if(registration.equalsIgnoreCase("true")) {
            model.addAttribute("registrationError", "Registration Error");
        }
        return "register";
    }

    @PostMapping("/registerUser")
    public String showLoginForm(@Valid @ModelAttribute("user") UserCreateDTO appUser, BindingResult br, Model model) {
        if (userRepository.existsByNameIgnoreCase(appUser.getName())) br.rejectValue("name", "duplicate", "This username is already taken");
        if (userRepository.existsByEmail(appUser.getEmail())) br.rejectValue("email", "duplicate", "An account with this email already exists");
        if (br.hasErrors()) return "register";

        try{
            abstractService.registerNewUser(appUser);
        }catch (Exception e){
            model.addAttribute("registrationError", "Registration failed: " + e.getMessage());
            return "register";
        }

        return "redirect:/login?registered=true";
    }

    @GetMapping("/forget-password")
    public String showForgetPasswordForm(Model model) {
        model.addAttribute("emailForm", new EmailForm());
        return "forget-password";
    }

    @PostMapping("/forget-password")
    public String checkEmailProcess(@Valid @ModelAttribute("emailForm") EmailForm emailForm, HttpSession session,BindingResult bindingResult) {
        Optional<User> userOp = userRepository.findByEmail(emailForm.getEmail());
        if (userOp.isEmpty()) bindingResult.rejectValue("email", "not found", "An account with this email doesn't exist");
        if (bindingResult.hasErrors()) return "forget-password";

        otpService.sendOtp(userOp.get());
        session.setAttribute("resetEmail", emailForm.getEmail());
        return "redirect:/confirm-otp";
    }

    @GetMapping("/confirm-otp")
    public String showConfirmOtpForm(HttpSession session) {
        String email = (String) session.getAttribute("resetEmail");

        if (email == null) {
            return "redirect:/forget-password";
        }
        return "confirm-otp";
    }

    @PostMapping("/confirm-otp")
    public String processOtp(@RequestParam("otp") String otp, HttpSession session, Model model) {

        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            model.addAttribute("otpError", "Session expired. Please try again.");
            return "forget-password";
        }

        try{
            otpService.isOtpValid(email, otp);
        }catch (OtpExpiredException e){
            model.addAttribute("otpError", "OTP is expired. Please try again.");
            return "confirm-otp";
        }catch (OtpInvalidException e){
            model.addAttribute("otpError", "Invalid OTP. Please try again.");
            return "confirm-otp";
        }

        return "redirect:/reset-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(Model model, HttpSession session) {
        String email = (String) session.getAttribute("resetEmail");

        if (email == null) {
            return "redirect:/forget-password";
        }
        model.addAttribute("resetPasswordForm", new ResetPasswordDTO());
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@ModelAttribute("resetPasswordForm") ResetPasswordDTO form, HttpSession session, Model model) {

        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            model.addAttribute("error", "Session expired. Please start again.");
            return "forget-password";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            return "reset-password";
        }

        authService.resetPassword(email, form.getPassword());
        session.removeAttribute("resetEmail");

        return "redirect:/login";
    }

}
