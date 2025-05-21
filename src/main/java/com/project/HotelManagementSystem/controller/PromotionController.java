package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    public String getAllPromotions(Model model) {
        model.addAttribute("promotions",this.promotionService.findAllPromotions());
        return "promotions/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("promotion",new Promotion());
        return "promotions/create";
    }

    @PostMapping("/create")
    public String createPromotion(@ModelAttribute Promotion promotion) {
        this.promotionService.createPromotion(promotion);
        return "redirect:/promotions";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("promotion",this.promotionService.findPromotionById(id));
        return "promotions/edit";
    }

    @PostMapping("/update/{id}")
    public String updatePromotion(@PathVariable Long id, @ModelAttribute Promotion promotion) {
        this.promotionService.updatePromotion(id, promotion);
        return "redirect:/promotions";
    }

    @GetMapping("/delete/{id}")
    public String deletePromotion(@PathVariable Long id) {
        this.promotionService.deletePromotion(id);
        return "redirect:/promotions";
    }
}
