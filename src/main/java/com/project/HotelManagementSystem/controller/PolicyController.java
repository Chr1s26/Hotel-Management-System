package com.project.HotelManagementSystem.controller;


import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.policy.PolicyDTO;
import com.project.HotelManagementSystem.dto.policy.PolicyResponse;
import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/policies")
public class PolicyController {

    private final PolicyService policyService;

    @GetMapping()
    public String getAllPolicies(Model model,
                                 @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                 @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                 @RequestParam(defaultValue = AppConstants.SORT_BY_Id,required = false) String sortBy,
                                 @RequestParam(defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder) {
        PolicyResponse response = this.policyService.findAllPoliciesWithPagination(pageNumber,pageSize,sortBy,sortOrder);
        List<PolicyDTO> policyDTOList = response.getPolicies();
        model.addAttribute("policies",policyDTOList);
        model.addAttribute("response",response);
        model.addAttribute("sortBy",sortBy);
        model.addAttribute("sortOrder",sortOrder);
        return "policies/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("policy", new Policy());
        return "policies/create";
    }

    @PostMapping("/create")
    public String createPolicy(@ModelAttribute Policy policy) {
        policyService.createPolicy(policy);
        return "redirect:/policies";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        model.addAttribute("policy",policyService.findPolicyById(id));
        return "policies/edit";
    }

    @PostMapping("/update/{id}")
    public String updatePolicy(@PathVariable Long id, @ModelAttribute Policy policy) {
        policyService.updatePolicy(id, policy);
        return "redirect:/policies";
    }

    @GetMapping("/delete/{id}")
    public String deletePolicy(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return "redirect:/policies";
    }
}