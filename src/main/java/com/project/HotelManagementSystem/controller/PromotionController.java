package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.promotion.PromotionCreateDTO;
import com.project.HotelManagementSystem.dto.promotion.PromotionDTO;
import com.project.HotelManagementSystem.dto.promotion.PromotionResponse;
import com.project.HotelManagementSystem.dto.promotion.PromotionUpdateDTO;
import com.project.HotelManagementSystem.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    public String getAllPromotions(Model model,
                                   @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                   @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                   @RequestParam(defaultValue = AppConstants.SORT_BY_Id,required = false) String sortBy,
                                   @RequestParam(defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder) {
        PromotionResponse response = this.promotionService.findAllPromotionsWithPagination(pageNumber,pageSize,sortBy,sortOrder);
        List<PromotionDTO> promotionDTOList = response.getPromotions();
        model.addAttribute("promotions",promotionDTOList);
        model.addAttribute("response",response);
        model.addAttribute("sortBy",sortBy);
        model.addAttribute("sortOrder",sortOrder);
        return "promotions/listing";
    }

    @GetMapping("/new")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showCreateForm(Model model) {
        model.addAttribute("promotion",new PromotionCreateDTO());
        return "promotions/create";
    }

    @PostMapping("/create")
    public String createPromotion(@Valid @ModelAttribute("promotion") PromotionCreateDTO promotionCreateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "promotions/create";
        }
        this.promotionService.createPromotion(promotionCreateDTO);
        return "redirect:/promotions";
    }

    @GetMapping("/edit/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("promotion",this.promotionService.findPromotionById(id));
        return "promotions/edit";
    }

    @PostMapping("/update/{id}")
    public String updatePromotion(@PathVariable Long id,@Valid @ModelAttribute("promotion") PromotionUpdateDTO promotionUpdateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "promotions/edit";
        }
        this.promotionService.updatePromotion(id, promotionUpdateDTO);
        return "redirect:/promotions";
    }

    @GetMapping("/delete/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String deletePromotion(@PathVariable Long id) {
        this.promotionService.deletePromotion(id);
        return "redirect:/promotions";
    }
}
