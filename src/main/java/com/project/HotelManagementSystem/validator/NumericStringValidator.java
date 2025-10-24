package com.project.HotelManagementSystem.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NumericStringValidator implements ConstraintValidator<NumericString, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        if(value == null || value.trim().isEmpty()){
            return true;
        }
        try{
            Long.parseLong(value);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}
