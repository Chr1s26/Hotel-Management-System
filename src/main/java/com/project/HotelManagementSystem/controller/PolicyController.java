package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.dto.policy.PolicyCreateDTO;
import com.project.HotelManagementSystem.dto.policy.PolicyUpdateDTO;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.policy.PolicySearchQuery;
import com.project.HotelManagementSystem.dto.searchFilter.policy.PolicySearchField;
import com.project.HotelManagementSystem.dto.searchFilter.policy.PolicySearchFilter;
import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.service.PolicyService;
import com.project.HotelManagementSystem.service.search.PolicySearchService;
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
@RequestMapping("/policies")
public class PolicyController {

    private final PolicyService policyService;
    private final PolicySearchService policySearchService;

    @ModelAttribute("query")
    public PolicySearchQuery initQuery() {
        PolicySearchQuery query = new PolicySearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new PolicySearchFilter(PolicySearchField.TITLE, MatchType.CONTAINS, ""),
                new PolicySearchFilter(PolicySearchField.APPLICABLE_TO,MatchType.CONTAINS,""),
                new PolicySearchFilter(PolicySearchField.STATUS,MatchType.EXACT,"")
        ));
        return query;
    }

    @GetMapping
    public String getAllPolicies(Model model, @ModelAttribute("query")PolicySearchQuery query) {
        Page<Policy> page = this.policySearchService.searchByQuery(query);
        model.addAttribute("policies", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "policies/listing";
    }
    
    @PostMapping
    public String searchPolicy(Model model, @ModelAttribute("query") PolicySearchQuery query) {
        Page<Policy> page = this.policySearchService.searchByQuery(query);
        model.addAttribute("policies", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "policies/listing";
    }

    @GetMapping("/new")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showCreateForm(Model model) {
        model.addAttribute("policy", new PolicyCreateDTO());
        return "policies/create";
    }

    @PostMapping("/create")
    public String createPolicy(@Valid @ModelAttribute("policy") PolicyCreateDTO policyCreateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "policies/create";
        }
        policyService.createPolicy(policyCreateDTO);
        return "redirect:/policies";
    }

    @GetMapping("/edit/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showUpdateForm(@PathVariable Long id, Model model) {
        model.addAttribute("policy",policyService.findPolicyById(id));
        return "policies/edit";
    }

    @PostMapping("/update/{id}")
    public String updatePolicy(@PathVariable Long id,@Valid @ModelAttribute("policy") PolicyUpdateDTO policyUpdateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "policies/edit";
        }
        policyService.updatePolicy(id, policyUpdateDTO);
        return "redirect:/policies";
    }

    @GetMapping("/delete/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String deletePolicy(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return "redirect:/policies";
    }
}