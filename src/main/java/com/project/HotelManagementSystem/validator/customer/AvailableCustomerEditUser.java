package com.project.HotelManagementSystem.validator.customer;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AvailableCustomerEditUserValidator.class)
public @interface AvailableCustomerEditUser {
    String message() default "Selected user is already linked to another customer.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
