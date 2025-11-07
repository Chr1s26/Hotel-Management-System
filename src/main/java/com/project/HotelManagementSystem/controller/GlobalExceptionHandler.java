package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.InvalidRoleException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.exception.UserNameNotFoundException;
import com.project.HotelManagementSystem.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    private final UserService userService;
    private final AddressService addressService;
    private final PromotionService promotionService;
    private final PropertyDescriptionService propertyDescriptionService;
    private final PolicyService policyService;
    private final HotelService hotelService;
    private final AmenitiesService amenitiesService;
    private final CityService cityService;
    private final CountryService countryService;
    private final RegionService regionService;

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        BindingResult br = new BeanPropertyBindingResult(ex.getObjectValue(), ex.getObjectName());
        Object rejected = new BeanWrapperImpl(ex.getObjectValue()).getPropertyValue(ex.getField());
        br.addError(new FieldError(
                ex.getObjectName(),
                ex.getField(),
                rejected,
                false,
                new String[]{ex.getMessageKey()},
                null,
                ex.getDefaultMessage()
        ));
        model.addAttribute(ex.getObjectName(), ex.getObjectValue());
        model.addAttribute(MODEL_KEY_PREFIX + ex.getObjectName(), br);
        model.addAttribute("requestURI", ex.getView());
        return ex.getView();
    }

    @ExceptionHandler(DuplicateException.class)
    public String handleDuplicateField(DuplicateException ex, Model model) {
        BindingResult br = new BeanPropertyBindingResult(ex.getObjectValue(), ex.getObjectName());
        Object rejected = new BeanWrapperImpl(ex.getObjectValue()).getPropertyValue(ex.getField());
        br.addError(new FieldError(
                ex.getObjectName(),
                ex.getField(),
                rejected,
                false,
                new String[]{ex.getMessageKey()},
                null,
                ex.getDefaultMessage()
        ));
        addAttributes(ex.getObjectName(),model);
        model.addAttribute(ex.getObjectName(), ex.getObjectValue());
        model.addAttribute(MODEL_KEY_PREFIX + ex.getObjectName(), br);
        model.addAttribute("requestURI", ex.getView());
        return ex.getView();
    }

    @ExceptionHandler(InvalidRoleException.class)
    public String handleInvalidRoleField(InvalidRoleException ex, Model model) {
        BindingResult br = new BeanPropertyBindingResult(ex.getObjectValue(), ex.getObjectName());
        Object rejected = new BeanWrapperImpl(ex.getObjectValue()).getPropertyValue(ex.getField());
        br.addError(new FieldError(
                ex.getObjectName(),
                ex.getField(),
                rejected,
                false,
                new String[]{ex.getMessageKey()},
                null,
                ex.getDefaultMessage()
        ));
        model.addAttribute(ex.getObjectName(), ex.getObjectValue());
        model.addAttribute(MODEL_KEY_PREFIX + ex.getObjectName(), br);
        model.addAttribute("requestURI", ex.getView());
        return ex.getView();
    }

    @ExceptionHandler(org.springframework.security.authentication.InternalAuthenticationServiceException.class)
    public String handleInternalAuthError(InternalAuthenticationServiceException ex, RedirectAttributes redirectAttributes, Model model) {

        if (ex.getCause() instanceof UserNameNotFoundException unameEx) {
            model.addAttribute("requestURI", unameEx.getView());
            redirectAttributes.addFlashAttribute("alertMessage", unameEx.getMessage());
            return "redirect:/" + unameEx.getView();
        }
        model.addAttribute("requestURI", "/login");
        redirectAttributes.addFlashAttribute("alertMessage", "Authentication failed");
        return "redirect:/login";
    }

    public void addAttributes(String object, Model model) {
        if(object.equalsIgnoreCase("admin") || object.equalsIgnoreCase("editor")) {
            model.addAttribute("users", userService.findAllUsers());
        }else if(object.equalsIgnoreCase("hotel")) {
            model.addAttribute("addresses", this.addressService.findAllAddress());
            model.addAttribute("propertyDescriptions", this.propertyDescriptionService.findAllPropertyDescriptions());
            model.addAttribute("promotions", this.promotionService.findAllPromotions());
            model.addAttribute("policies", this.policyService.findAllPolicies());
        }else if(object.equalsIgnoreCase("room")) {
            model.addAttribute("hotels", hotelService.findAllHotels());
            model.addAttribute("amenities", amenitiesService.findAllAmenities());
            model.addAttribute("promotions", promotionService.findAllPromotions());
        }else if(object.equalsIgnoreCase("address")) {
            model.addAttribute("cities", cityService.findAllCities());
        }else if(object.equalsIgnoreCase("region")) {
            model.addAttribute("countries", countryService.findAllCountries());
        }else if(object.equalsIgnoreCase("city")) {
            model.addAttribute("regions", regionService.findAllRegion());
        }
    }

}
