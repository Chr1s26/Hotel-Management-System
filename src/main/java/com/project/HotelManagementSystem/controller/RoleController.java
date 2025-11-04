package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.role.RoleCreateDTO;
import com.project.HotelManagementSystem.dto.role.RoleDTO;
import com.project.HotelManagementSystem.dto.role.RoleResponse;
import com.project.HotelManagementSystem.dto.role.RoleUpdateDTO;
import com.project.HotelManagementSystem.dto.room.RoomCreateDTO;
import com.project.HotelManagementSystem.dto.room.RoomDTO;
import com.project.HotelManagementSystem.dto.room.RoomResponse;
import com.project.HotelManagementSystem.dto.room.RoomUpdateDTO;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.role.RoleSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.role.RoleSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.role.RoleSearchQuery;
import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.service.RoleService;
import com.project.HotelManagementSystem.service.excelExport.RoleExportProcess;
import com.project.HotelManagementSystem.service.search.RoleSearchService;
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
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
    private final RoleSearchService roleSearchService;
    private final RoleExportProcess roleExportProcess;

    @ModelAttribute("query")
    public RoleSearchQuery initQuery() {
        RoleSearchQuery query = new RoleSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new RoleSearchFilter(RoleSearchField.ROLE_NAME, MatchType.CONTAINS,""),
                new RoleSearchFilter(RoleSearchField.STATUS, MatchType.EXACT, "")
        ));
        return query;
    }

    @GetMapping
    public String getAllRoles(Model model, @ModelAttribute("query") RoleSearchQuery query) {
        Page<Role> page = this.roleSearchService.searchByQuery(query);
        model.addAttribute("roles",page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "roles/listing";
    }

    @PostMapping
    public String searchRoles(Model model, @ModelAttribute("query") RoleSearchQuery query) {
        Page<Role> page = this.roleSearchService.searchByQuery(query);
        model.addAttribute("roles",page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "roles/listing";
    }

    @GetMapping("/new")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showCreateForm(Model model) {
        model.addAttribute("role", new RoleCreateDTO());
        return "roles/create";
    }

    @PostMapping("/create")
    public String createRole(@Valid @ModelAttribute("role") RoleCreateDTO roleCreateDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "roles/create";
        }
        roleService.createRole(roleCreateDTO);
        return "redirect:/roles";
    }

    @GetMapping("/edit/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("role", roleService.findRoleById(id));
        return "roles/edit";
    }

    @PostMapping("/update/{id}")
    public String updateRole(@PathVariable Long id, @Valid @ModelAttribute("role") RoleUpdateDTO roleUpdateDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "roles/edit";
        }
        roleService.updateRole(id, roleUpdateDTO);
        return "redirect:/roles";
    }

    @GetMapping("/delete/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return "redirect:/roles";
    }

    @PostMapping("/export/excel")
    public String exportExcelToS3(Model model, @ModelAttribute("query") RoleSearchQuery query) {
        roleExportProcess.generateExportFile(query);
        return "redirect:/roles";
    }
}
