package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.dto.customer.CustomerCreateDTO;
import com.project.HotelManagementSystem.dto.customer.CustomerUpdateDTO;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.customer.CustomerSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.customer.CustomerSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.customer.CustomerSearchQuery;
import com.project.HotelManagementSystem.entity.Customer;
import com.project.HotelManagementSystem.service.CustomerService;
import com.project.HotelManagementSystem.service.UserService;
import com.project.HotelManagementSystem.service.excelExport.CustomerExportProcess;
import com.project.HotelManagementSystem.service.search.CustomerSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerSearchService customerSearchService;
    @Autowired
    private CustomerExportProcess customerExportProcess;

    @ModelAttribute("query")
    public CustomerSearchQuery initQuery() {
        CustomerSearchQuery query = new CustomerSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new CustomerSearchFilter(CustomerSearchField.NAME, MatchType.CONTAINS, ""),
                new CustomerSearchFilter(CustomerSearchField.PHONE, MatchType.CONTAINS, ""),
                new CustomerSearchFilter(CustomerSearchField.NATIONALITY, MatchType.CONTAINS, ""),
                new CustomerSearchFilter(CustomerSearchField.DATE_OF_BIRTH, MatchType.EXACT,""),
                new CustomerSearchFilter(CustomerSearchField.VIP_STATUS, MatchType.EXACT, ""),
                new CustomerSearchFilter(CustomerSearchField.STATUS, MatchType.EXACT, "")
        ));
        return query;
    }

    @GetMapping
    @ActiveRole("ADMIN")
    public String getAllCustomers(Model model, @ModelAttribute("query") CustomerSearchQuery query) {
        Page<Customer> page = customerSearchService.searchByQuery(query);
        model.addAttribute("customers", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "customers/listing";
    }

    @PostMapping
    @ActiveRole("ADMIN")
    public String searchCustomers(Model model, @ModelAttribute("query") CustomerSearchQuery query) {
        Page<Customer> page = customerSearchService.searchByQuery(query);
        model.addAttribute("customers", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "customers/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("users",userService.findAllUsers());
        return "customers/create";
    }

    @PostMapping("/create")
    public String createCustomer(@Valid @ModelAttribute("customer") CustomerCreateDTO customerCreateDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAllUsers());
            return "customers/create";
        }
        customerService.createCustomer(customerCreateDTO);
        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String editCustomer(@PathVariable("id") Long id, Model model) {
        model.addAttribute("customer", customerService.findCustomerById(id));
        model.addAttribute("users", userService.findAllUsers());
        return "customers/edit";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable("id") Long id, @Valid @ModelAttribute("customer") CustomerUpdateDTO customerUpdateDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAllUsers());
            return "customers/edit";
        }
        customerService.updateCustomer(id, customerUpdateDTO);
        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }

    @PostMapping("/export/excel")
    public String exportExcel(Model model, @ModelAttribute("query") CustomerSearchQuery query) {
        customerExportProcess.generateExportFile(query);
        return "redirect:/customers";
    }

    @GetMapping("/view/{id}")
    public String viewCustomer(@PathVariable("id") Long id, Model model) {
        model.addAttribute("customer", customerService.findCustomerById(id));
        return "customers/view";
    }
}
