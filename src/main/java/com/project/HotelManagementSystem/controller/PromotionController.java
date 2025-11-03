package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.promotion.PromotionCreateDTO;
import com.project.HotelManagementSystem.dto.promotion.PromotionDTO;
import com.project.HotelManagementSystem.dto.promotion.PromotionResponse;
import com.project.HotelManagementSystem.dto.promotion.PromotionUpdateDTO;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.promotion.PromotionSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.promotion.PromotionSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.promotion.PromotionSearchQuery;
import com.project.HotelManagementSystem.dto.searchFilter.user.UserSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.user.UserSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.user.UserSearchQuery;
import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.service.PromotionService;
import com.project.HotelManagementSystem.service.search.PromotionSearchService;
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
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionService promotionService;
    private final PromotionSearchService promotionSearchService;

    @ModelAttribute("query")
    public PromotionSearchQuery initQuery() {
        PromotionSearchQuery query = new PromotionSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new PromotionSearchFilter(PromotionSearchField.CODE, MatchType.CONTAINS, ""),
                new PromotionSearchFilter(PromotionSearchField.DISCOUNT_TYPE,MatchType.EXACT,""),
                new PromotionSearchFilter(PromotionSearchField.DISCOUNT_AMOUNT,MatchType.EXACT,""),
                new PromotionSearchFilter(PromotionSearchField.START_DATE,MatchType.EXACT,""),
                new PromotionSearchFilter(PromotionSearchField.END_DATE,MatchType.EXACT,""),
                new PromotionSearchFilter(PromotionSearchField.STATUS,MatchType.EXACT,"")
        ));
        return query;
    }


    @GetMapping
    public String getAllPromotions(Model model,@ModelAttribute("query") PromotionSearchQuery query) {
        Page<Promotion> page = this.promotionSearchService.searchByQuery(query);
        model.addAttribute("promotions", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "promotions/listing";
    }

    @PostMapping
    public String searchPromotions(@ModelAttribute("query") PromotionSearchQuery query,Model model){
        Page<Promotion> page = this.promotionSearchService.searchByQuery(query);
        model.addAttribute("promotions", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
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
