package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Resource not found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "";
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
        model.addAttribute(ex.getObjectName(), ex.getObjectValue());
        model.addAttribute(MODEL_KEY_PREFIX + ex.getObjectName(), br);

        return ex.getView();
    }
}
