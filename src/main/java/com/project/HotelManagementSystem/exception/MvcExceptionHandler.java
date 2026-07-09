package com.project.HotelManagementSystem.exception;

import com.project.HotelManagementSystem.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.hibernate.PropertyValueException;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

@ControllerAdvice(
        annotations = Controller.class
)
@AllArgsConstructor
public class MvcExceptionHandler {

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

    @ExceptionHandler(RelationshipInUseException.class)
    public String handleRelationshipInUse(RelationshipInUseException ex,
                                          RedirectAttributes redirectAttributes,
                                          HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("alertMessage", ex.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public String handleDataIntegrity(org.springframework.dao.DataIntegrityViolationException ex,
                                      RedirectAttributes redirectAttributes,
                                      HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("alertMessage",
                "This record can't be permanently deleted because other records still depend on it. "
                        + "Remove or reassign those first, or use Soft delete instead.");
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
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

    @ExceptionHandler(MissingIdentificationException.class)
    public String handleMissingId(MissingIdentificationException ex, Model model) {
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

    @ExceptionHandler(ExportFailedException.class)
    public String handleExportFailed(ExportFailedException ex, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("exportError",
                ex.getDefaultMessage() != null ? ex.getDefaultMessage() : "Export failed.");

        return "redirect:" + (ex.getView() != null ? ex.getView() : "");
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


    @ExceptionHandler(FileNotUploadException.class)
    public ModelAndView myApiException(FileNotUploadException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorTitle", "There is no picture is selected to upload");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("requestURI", request.getRequestURL());
        return modelAndView;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView myResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorTitle", "Resource Not Found");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("requestURI", request.getRequestURL());
        return modelAndView;
    }

    @ExceptionHandler(ApiException.class)
    public ModelAndView myApiException(ApiException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorTitle", "API Error");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("requestURI", request.getRequestURL());
        return modelAndView;
    }

    public ModelAndView myDuplicateException(DuplicateException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorTitle", "Duplicate Record");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("requestURI", request.getRequestURL());
        return modelAndView;
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ModelAndView myInvalidDataAccessApiUsageViolationException(InvalidDataAccessApiUsageException ex, HttpServletRequest request) {
        String message = "Cannot delete this item because it is being used elsewhere.";

        if(ex.getCause() != null && ex.getCause().getMessage().contains("foreign key")){
            message = "Delete failed due to database foreign key constraint violation";
        }

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorTitle", "Data Integrity Violation");
        modelAndView.addObject("errorMessage",message);
        modelAndView.addObject("requestURI", request.getRequestURL());
        return modelAndView;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView myConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorTitle", "Constraint Violation");
        modelAndView.addObject("errorMessage", "You try to use an entity that is already used by another entity");
        modelAndView.addObject("requestURI", request.getRequestURL());
        return modelAndView;
    }

    @ExceptionHandler(PropertyValueException.class)
    public ModelAndView myPropertyValueException(PropertyValueException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorTitle", "Property Value Violation");
        modelAndView.addObject("errorMessage", "You try to persist an entity with a null required field");
        modelAndView.addObject("requestURI", request.getRequestURL());
        return modelAndView;
    }

    @ExceptionHandler(UserNameNotFoundException.class)
    public ModelAndView handle(UserNameNotFoundException ex,
                               HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("errorTitle", "User Not Found");
        mv.addObject("errorMessage", ex.getMessage());
        mv.addObject("requestURI", request.getRequestURL());
        return mv;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorTitle", "Unexpected Error");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("requestURI", request.getRequestURL());
        return modelAndView;
    }

}
