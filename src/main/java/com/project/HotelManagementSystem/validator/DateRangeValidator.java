package com.project.HotelManagementSystem.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.temporal.Temporal;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startField;
    private String endField;
    private boolean allowEqual;

    @Override
    public void initialize(ValidDateRange anno) {
        this.startField = anno.start();
        this.endField = anno.end();
        this.allowEqual = anno.allowEqual();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext ctx) {
        if (value == null) return true;

        BeanWrapperImpl bean = new BeanWrapperImpl(value);
        Object startObj = bean.getPropertyValue(startField);
        Object endObj   = bean.getPropertyValue(endField);

        if (startObj == null || endObj == null) return true;

        if (!(startObj instanceof Temporal) || !(endObj instanceof Temporal)) return true;

        @SuppressWarnings("unchecked")
        Comparable<Object> start = (Comparable<Object>) startObj;
        Comparable<Object> end   = (Comparable<Object>) endObj;

        boolean ok = allowEqual ? (start.compareTo(end) <= 0) : (start.compareTo(end) < 0);
        if (ok) return true;

        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                .addPropertyNode(endField)
                .addConstraintViolation();
        return false;
    }
}
