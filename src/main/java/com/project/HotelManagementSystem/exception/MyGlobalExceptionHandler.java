package com.project.HotelManagementSystem.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.PropertyValueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class MyGlobalExceptionHandler {

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView myDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorTitle", "Data Integrity Violation");
        modelAndView.addObject("errorMessage", "null foreign key, or duplicate unique key");
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

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorTitle", "Unexpected Error");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("requestURI", request.getRequestURL());
        return modelAndView;
    }

}
