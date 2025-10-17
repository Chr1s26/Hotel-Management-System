package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.admin.*;
import com.project.HotelManagementSystem.entity.constants.AdminType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.service.AdminService;
import com.project.HotelManagementSystem.service.AuthService;
import com.project.HotelManagementSystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @GetMapping
    @ActiveRole("ADMIN")
    public String getAllAdmins(Model model,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) LocalDate dateOfBirth,
                               @RequestParam(required = false) String nationality,
                               @RequestParam(required = false) String passportNumber,
                               @RequestParam(required = false) String nationalIdNumber,
                               @RequestParam(required = false) AdminType adminType,
                               @RequestParam(required = false) StatusType statusType,
                               @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                               @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                               @RequestParam(defaultValue = AppConstants.SORT_BY_Id,required = false) String sortBy,
                               @RequestParam(defaultValue = AppConstants.SORT_ORDER) String sortOrder) {

        AdminSearchCriteria adminSearchCriteria = new AdminSearchCriteria();
        adminSearchCriteria.setName(name);
        adminSearchCriteria.setPhone(phone);
        adminSearchCriteria.setDateOfBirth(dateOfBirth);
        adminSearchCriteria.setNationality(nationality);
        adminSearchCriteria.setPassportNumber(passportNumber);
        adminSearchCriteria.setNationalIdNumber(nationalIdNumber);
        adminSearchCriteria.setAdminType(adminType);
        adminSearchCriteria.setStatusType(statusType);
        adminSearchCriteria.setPageNumber(pageNumber);
        adminSearchCriteria.setPageSize(pageSize);
        adminSearchCriteria.setSortBy(sortBy);
        adminSearchCriteria.setSortOrder(sortOrder);
        AdminResponse adminResponse = adminService.search(adminSearchCriteria);
        List<AdminDTO> admins = adminResponse.getAdmins();
        model.addAttribute("admins", admins);
        model.addAttribute("response", adminResponse);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("sortBy", sortBy);
        return "admins/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("admin", new AdminCreateDTO());
        model.addAttribute("users", userService.findAllUsers());
        return "admins/create";
    }

    @PostMapping("/create")
    public String createAdmin(@Valid AdminCreateDTO adminCreateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAllUsers());
            return "admins/create";
        }
        adminService.createAdmin(adminCreateDTO);
        return "redirect:/admins";
    }

    @GetMapping("/edit/{id}")
    public String editAdmin(@PathVariable Long id, Model model) {
        model.addAttribute("admin",adminService.findAdminById(id));
        model.addAttribute("users", userService.findAllUsers());
        return "admins/edit";
    }

    @PostMapping("/update/{id}")
    public String updateAdmin(@PathVariable Long id, @Valid @ModelAttribute("admin") AdminUpdateDTO adminUpdateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("admin",adminService.findAdminById(id));
            model.addAttribute("users", userService.findAllUsers());
            return "admins/edit";
        }
        adminService.updateAdmin(id, adminUpdateDTO);
        return "redirect:/admins";
    }

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return "redirect:/admins";
    }
}
