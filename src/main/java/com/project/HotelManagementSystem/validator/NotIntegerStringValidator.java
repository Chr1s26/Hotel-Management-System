package com.project.HotelManagementSystem.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotIntegerStringValidator implements ConstraintValidator<NotIntegerString, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.isEmpty()){
            return true;
        }
        try{
            Integer.parseInt(value);
            return false;
        }catch (NumberFormatException e){
            return true;
        }
    }


}
