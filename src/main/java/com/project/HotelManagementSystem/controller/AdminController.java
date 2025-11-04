package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.dto.admin.*;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.admin.AdminSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.admin.AdminSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.admin.AdminSearchQuery;
import com.project.HotelManagementSystem.entity.Admin;
import com.project.HotelManagementSystem.service.AdminService;
import com.project.HotelManagementSystem.service.UserService;
import com.project.HotelManagementSystem.service.excelExport.AdminExportProcess;
import com.project.HotelManagementSystem.service.search.AdminSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    private AdminSearchService adminSearchService;
    @Autowired
    private AdminExportProcess adminExportProcess;

    @ModelAttribute("query")
    public AdminSearchQuery initQuery() {
        AdminSearchQuery query = new AdminSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new AdminSearchFilter(AdminSearchField.NAME, MatchType.CONTAINS,""),
                new AdminSearchFilter(AdminSearchField.PHONE, MatchType.CONTAINS, ""),
                new AdminSearchFilter(AdminSearchField.NATIONALITY, MatchType.CONTAINS, ""),
                new AdminSearchFilter(AdminSearchField.PASSPORT_NUMBER, MatchType.CONTAINS, ""),
                new AdminSearchFilter(AdminSearchField.NATIONAL_ID_NUMBER, MatchType.CONTAINS, ""),
                new AdminSearchFilter(AdminSearchField.DATE_OF_BIRTH, MatchType.EXACT, ""),
                new AdminSearchFilter(AdminSearchField.ADMIN_TYPE, MatchType.EXACT, ""),
                new AdminSearchFilter(AdminSearchField.STATUS, MatchType.EXACT,"")
                ));
        return query;
    }

    @GetMapping
    @ActiveRole("ADMIN")
    public String getAllAdmins(Model model, @ModelAttribute("query") AdminSearchQuery query) {
        Page<Admin> page = adminSearchService.searchByQuery(query);
        model.addAttribute("admins", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "admins/listing";
    }

    @PostMapping
    @ActiveRole("ADMIN")
    public String searchAdmins(Model model, @ModelAttribute("query") AdminSearchQuery query) {
        Page<Admin> page = adminSearchService.searchByQuery(query);
        model.addAttribute("admins", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "admins/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("admin", new AdminCreateDTO());
        model.addAttribute("users", userService.findAllUsers());
        return "admins/create";
    }

    @PostMapping("/create")
    public String createAdmin(@Valid @ModelAttribute("admin") AdminCreateDTO adminCreateDTO, BindingResult bindingResult, Model model) {
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
//            model.addAttribute("admin",adminService.findAdminById(id));
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

    @PostMapping("/export/excel")
    public String exportExcel(Model model, @ModelAttribute("query") AdminSearchQuery query) {
        adminExportProcess.generateExportFile(query);
        return "redirect:/admins";
    }
}
