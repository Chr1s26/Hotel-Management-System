package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.helper.StringUtil;
import com.project.HotelManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", this.userService.findAllUsers());
        return "users/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "users/create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user,Model model) {
        boolean hasError = false;
        if(StringUtil.isEmpty(user.getName())) {
            model.addAttribute("nameError", "Please enter your name");
            hasError = true;
        }
        if(StringUtil.isEmpty(user.getPhone())) {
            model.addAttribute("phoneError", "Please enter your phone");
            hasError = true;
        }
        if(StringUtil.isEmpty(user.getEmail())) {
            model.addAttribute("emailError", "Please enter your email");
            hasError = true;
        }
        if(StringUtil.isEmpty(user.getPassword())) {
            model.addAttribute("passwordError", "Please enter your password");
            hasError = true;
        }
        if(StringUtil.isEmpty(user.getNationality())) {
            model.addAttribute("nationalityError", "Please enter your nationality");
            hasError = true;
        }

        if(hasError) {
            model.addAttribute("user", user);
            return "users/create";
        }
        this.userService.createUser(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("user",this.userService.findUserById(id));
        return "users/edit";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        this.userService.updateUser(id,user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return "redirect:/users";
    }
}
