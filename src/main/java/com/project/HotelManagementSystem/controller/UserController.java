package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.user.UserCreateDTO;
import com.project.HotelManagementSystem.dto.user.UserDTO;
import com.project.HotelManagementSystem.dto.user.UserResponse;
import com.project.HotelManagementSystem.dto.user.UserUpdateDTO;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.helper.StringUtil;
import com.project.HotelManagementSystem.service.PromotionService;
import com.project.HotelManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public String getAllUsers(Model model,
                              @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                              @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                              @RequestParam(defaultValue = AppConstants.SORT_BY_Id) String sortBy,
                              @RequestParam(defaultValue = AppConstants.SORT_ORDER) String sortOrder) {
        UserResponse userResponse = this.userService.findAllUsersWithPagination(pageNumber,pageSize,sortBy,sortOrder);
        List<UserDTO> userDTOList = userResponse.getUsers();
        model.addAttribute("users", userDTOList);
        model.addAttribute("response",userResponse);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        return "users/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        model.addAttribute("promotions", promotionService.findAllPromotions());
        return "users/create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute UserCreateDTO userCreateDTO,Model model) {
        boolean hasError = false;
        if(StringUtil.isEmpty(userCreateDTO.getName())) {
            model.addAttribute("nameError", "Please enter your name");
            hasError = true;
        }
        if(StringUtil.isEmpty(userCreateDTO.getPhone())) {
            model.addAttribute("phoneError", "Please enter your phone");
            hasError = true;
        }
        if(StringUtil.isEmpty(userCreateDTO.getEmail())) {
            model.addAttribute("emailError", "Please enter your email");
            hasError = true;
        }
        if(StringUtil.isEmpty(userCreateDTO.getPassword())) {
            model.addAttribute("passwordError", "Please enter your password");
            hasError = true;
        }
        if(StringUtil.isEmpty(userCreateDTO.getNationality())) {
            model.addAttribute("nationalityError", "Please enter your nationality");
            hasError = true;
        }

        if(hasError) {
            model.addAttribute("user", userCreateDTO);
            return "users/create";
        }
        this.userService.createUser(userCreateDTO);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("user",this.userService.findUserById(id));
        model.addAttribute("promotions", promotionService.findAllPromotions());
        return "users/edit";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserUpdateDTO userUpdateDTO, BindingResult bindingResul, Model model) {
        if(bindingResul.hasErrors()) {
            model.addAttribute("promotions", promotionService.findAllPromotions());
        }
        this.userService.updateUser(id,userUpdateDTO);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return "redirect:/users";
    }
}
