package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.dto.user.UserCreateDTO;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.service.AbstractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class AuthController {
    private final AbstractService abstractService;
    private final UserRepository userRepository;
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

}
