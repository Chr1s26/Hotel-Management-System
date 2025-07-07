package com.project.HotelManagementSystem.validator;

import com.project.HotelManagementSystem.dto.promotion.PromotionCreateDTO;
import com.project.HotelManagementSystem.dto.promotion.PromotionUpdateDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        LocalDate startDate = null;
        LocalDate endDate = null;

        if(obj instanceof PromotionCreateDTO dto){
            startDate = dto.getStartDate();
            endDate = dto.getEndDate();
        }else if(obj instanceof PromotionUpdateDTO dto){
            startDate = dto.getStartDate();
            endDate = dto.getEndDate();
        }

        if(startDate == null && endDate == null){
            return true;
        }

        if(!startDate.isBefore(endDate)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start date should be before end date").addPropertyNode("startDate").addConstraintViolation();
            return false;
        }
        return true;
    }
}
