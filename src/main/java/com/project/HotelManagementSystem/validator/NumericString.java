package com.project.HotelManagementSystem.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {
        NumericStringValidator.class
        ,NumericIntegerValidator.class
        , NumericDoubleValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NumericString {
    String message() default "Value must be a valid number (Integer or Decimal)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
