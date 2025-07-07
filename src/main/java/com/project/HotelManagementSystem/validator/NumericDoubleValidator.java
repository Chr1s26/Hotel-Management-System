package com.project.HotelManagementSystem.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NumericDoubleValidator implements ConstraintValidator<NumericString, Double> {
    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return value != null;
    }
}

