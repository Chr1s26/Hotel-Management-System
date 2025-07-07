package com.project.HotelManagementSystem.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CoordinateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCoordinates {
    String message() default "Invalid Coordinates Values";
    boolean isLatitude() default true;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
