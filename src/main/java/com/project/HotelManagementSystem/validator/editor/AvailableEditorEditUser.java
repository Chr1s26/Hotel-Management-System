package com.project.HotelManagementSystem.validator.editor;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AvailableEditorEditUserValidator.class)
public @interface AvailableEditorEditUser {
    String message() default "Selected user is already linked to another editor.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
