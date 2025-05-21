package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.PropertyDescription;
import com.project.HotelManagementSystem.service.PropertyDescriptionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/propertyDescriptions")
public class PropertyDescriptionController {

    private final PropertyDescriptionService propertyDescriptionService;

    @GetMapping
    public String getAllPropertyDescriptions(Model model) {
        model.addAttribute("propertyDescriptions", propertyDescriptionService.findAllPropertyDescriptions());
        return "propertyDescriptions/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("propertyDescription", new PropertyDescription());
        return "propertyDescriptions/create";
    }

    @PostMapping("/create")
    public String createPropertyDescription(@ModelAttribute PropertyDescription propertyDescription) {
        this.propertyDescriptionService.createPropertyDescription(propertyDescription);
        return "redirect:/propertyDescriptions";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("propertyDescription", propertyDescriptionService.findPropertyDescriptionById(id));
        return "propertyDescriptions/edit";
    }

    @PostMapping("/update/{id}")
    public String updatePropertyDescription(@PathVariable Long id, @ModelAttribute PropertyDescription propertyDescription) {
        this.propertyDescriptionService.updatePropertyDescription(id, propertyDescription);
        return "redirect:/propertyDescriptions";
    }

    @GetMapping
    public String deletePropertyDescription(@PathVariable Long id) {
        this.propertyDescriptionService.deletePropertyDescription(id);
        return "redirect:/propertyDescriptions";
    }
}
