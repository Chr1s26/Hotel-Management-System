package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.searchFilter.user.UserSearchQuery;
import com.project.HotelManagementSystem.dto.user.*;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.helper.StringUtil;
import com.project.HotelManagementSystem.service.PromotionService;
import com.project.HotelManagementSystem.service.RoleService;
import com.project.HotelManagementSystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PromotionService promotionService;
    private final RoleService roleService;

    @PostMapping
    public String getAllUsers(Model model, @ModelAttribute("query")UserSearchQuery query) {
        Page<User> users = userService.searchByQuery(query);
        return "users/listing";
    }

    @GetMapping("/new")
    @ActiveRole("ADMIN")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserCreateDTO());
//        model.addAttribute("roles", roleService.getAllRoles());
        return "users/create";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("user") UserCreateDTO userCreateDTO,BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
//            model.addAttribute("roles", roleService.getAllRoles());
            return "users/create";
        }
        this.userService.createUser(userCreateDTO);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("user",this.userService.findUserById(id));
//        model.addAttribute("roles", roleService.getAllRoles());
        return "users/edit";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,@Valid @ModelAttribute("user") UserUpdateDTO userUpdateDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
//            model.addAttribute("roles", roleService.getAllRoles());
            return "users/edit";
        }
        this.userService.updateUser(id,userUpdateDTO);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    @ActiveRole("ADMIN")
    public String deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return "redirect:/users";
    }
}
