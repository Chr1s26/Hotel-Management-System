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

    @GetMapping("/register/admin")
    public String showAdminRegisterForm(Model model) {
        Admin admin = new Admin();
        admin.setUser(new User());
        model.addAttribute("admin", admin);
        return "admin_register";
    }

    @PostMapping("/register/admin")
    public String processAdminRegistration(@ModelAttribute("admin") Admin admin, Model model) {
        try {

            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("registrationError", e.getMessage());
            return "admin_register";
        }
    }

    @GetMapping("/register/editor")
    public String showEditorRegisterForm(Model model) {
        Editor editor = new Editor();
        editor.setUser(new User());
        model.addAttribute("editor", editor);
        return "editor_register";
    }

    @PostMapping("/register/editor")
    public String processEditorRegistration(@ModelAttribute("editor") Editor editor, Model model) {
        try {

            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("registrationError", e.getMessage());
            return "editor_register";
        }
    }
}
