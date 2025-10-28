package com.project.HotelManagementSystem.validator.admin;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AvailableAdminEditUserValidator.class)
public @interface AvailableAdminEditUser {
    String message() default "Selected user is already linked to another admin.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
