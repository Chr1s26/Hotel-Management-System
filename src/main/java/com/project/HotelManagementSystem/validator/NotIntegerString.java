package com.project.HotelManagementSystem.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotIntegerStringValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotIntegerString {
    String message() default "Value cannot be an integer";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
