package com.project.HotelManagementSystem.validator.editor;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AvailableEditorCreateUserValidator.class)
public @interface AvailableEditorCreateUser {
    String message() default "Selected user is already linked to another editor.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
