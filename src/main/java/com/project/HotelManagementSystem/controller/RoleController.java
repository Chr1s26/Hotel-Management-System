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
import com.project.HotelManagementSystem.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public String getAllRoles(Model model,
                              @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                              @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                              @RequestParam(defaultValue = AppConstants.SORT_BY_Id) String sortBy,
                              @RequestParam(defaultValue = AppConstants.SORT_ORDER) String sortOrder) {
        RoleResponse roleResponse = roleService.findAllRolesWithPagination(pageNumber,pageSize,sortBy,sortOrder);
        List<RoleDTO> roleDTOList = roleResponse.getRoles();
        model.addAttribute("roles",roleDTOList);
        model.addAttribute("response",roleResponse);
        model.addAttribute("sortBy",sortBy);
        model.addAttribute("sortOrder",sortOrder);
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
}
