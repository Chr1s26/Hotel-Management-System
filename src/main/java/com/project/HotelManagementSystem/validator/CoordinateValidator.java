package com.project.HotelManagementSystem.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CoordinateValidator implements ConstraintValidator<ValidCoordinates,Double> {

    private boolean isLatitude;

    public void initialize(ValidCoordinates constraintAnnotation) {
        isLatitude = constraintAnnotation.isLatitude();
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if(value == null) return true;

        if(isLatitude) {
            return value >= -90 && value <= 90;
        }else{
            return value >= -180 && value <= 180;
        }
    }
}
