package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionCreateDTO;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionDTO;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionResponse;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionUpdateDTO;
import com.project.HotelManagementSystem.service.PropertyDescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/propertyDescriptions")
public class PropertyDescriptionController {

    private final PropertyDescriptionService propertyDescriptionService;

    @GetMapping
    public String getAllPropertyDescriptions(Model model,
                                             @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                             @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                             @RequestParam(defaultValue = AppConstants.SORT_BY_Id) String sortBy,
                                             @RequestParam(defaultValue = AppConstants.SORT_ORDER) String sortOrder) {
        PropertyDescriptionResponse propertyDescriptionResponse = propertyDescriptionService.findAllPropertyDescriptionsWithPagination(pageNumber,pageSize,sortBy,sortOrder);
        List<PropertyDescriptionDTO> propertyDescriptionDTOList = propertyDescriptionResponse.getPropertyDescriptions();
        model.addAttribute("propertyDescriptions", propertyDescriptionDTOList);
        model.addAttribute("response",propertyDescriptionResponse);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        return "propertyDescriptions/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("propertyDescription", new PropertyDescriptionCreateDTO());
        return "propertyDescriptions/create";
    }

    @PostMapping("/create")
    public String createPropertyDescription(@Valid @ModelAttribute("propertyDescription") PropertyDescriptionCreateDTO propertyDescriptionCreateDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "propertyDescriptions/create";
        }
        this.propertyDescriptionService.createPropertyDescription(propertyDescriptionCreateDTO);
        return "redirect:/propertyDescriptions";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("propertyDescription", propertyDescriptionService.findPropertyDescriptionById(id));
        return "propertyDescriptions/edit";
    }

    @PostMapping("/update/{id}")
    public String updatePropertyDescription(@PathVariable Long id,@Valid @ModelAttribute("propertyDescription") PropertyDescriptionUpdateDTO propertyDescriptionUpdateDTO,BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "propertyDescriptions/edit";
        }
        this.propertyDescriptionService.updatePropertyDescription(id, propertyDescriptionUpdateDTO);
        return "redirect:/propertyDescriptions";
    }

    @GetMapping("/delete/{id}")
    public String deletePropertyDescription(@PathVariable Long id) {
        this.propertyDescriptionService.deletePropertyDescription(id);
        return "redirect:/propertyDescriptions";
    }
}
