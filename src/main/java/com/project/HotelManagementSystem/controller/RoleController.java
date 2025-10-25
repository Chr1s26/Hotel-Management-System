package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
//
//    @ModelAttribute("query")
//    public RoleSearchQuery initQuery(){
//
//    }
}
