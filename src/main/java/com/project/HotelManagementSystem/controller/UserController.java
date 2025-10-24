package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.user.*;
import com.project.HotelManagementSystem.dto.user.*;
import com.project.HotelManagementSystem.entity.User;
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

    @ModelAttribute("query")
    public UserSearchQuery initQuery() {
        UserSearchQuery query = new UserSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new UserSearchFilter(UserSearchField.NAME, MatchType.CONTAINS, ""),
                new UserSearchFilter(UserSearchField.EMAIL, MatchType.CONTAINS, ""),
                new UserSearchFilter(UserSearchField.STATUS, MatchType.EXACT, "")
        ));
        return query;
    }

    @GetMapping
    public String getAllUsers(Model model, @ModelAttribute("query") UserSearchQuery query) {
        Page<User> page = userService.searchByQuery(query);
        model.addAttribute("users", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "users/listing";
    }

    @PostMapping
    public String searchUsers(Model model, @ModelAttribute("query") UserSearchQuery query) {
        Page<User> users = userService.searchByQuery(query);
        model.addAttribute("users", users.getContent());
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("totalElements", users.getTotalElements());
        return "users/listing";
    }

    @GetMapping("/new")
    @ActiveRole("ADMIN")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        return "users/create";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("user") UserCreateDTO userCreateDTO,BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "users/create";
        }
        this.userService.createUser(userCreateDTO);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("user",this.userService.findUserById(id));
        return "users/edit";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,@Valid @ModelAttribute("user") UserUpdateDTO userUpdateDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
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
