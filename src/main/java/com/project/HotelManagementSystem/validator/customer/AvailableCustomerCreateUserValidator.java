package com.project.HotelManagementSystem.validator.customer;

import com.project.HotelManagementSystem.repository.CustomerRepository;
import com.project.HotelManagementSystem.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AvailableCustomerCreateUserValidator implements ConstraintValidator<AvailableCustomerCreateUser, Long> {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext ctx) {
        if(userId == null) return false;

        if (!userRepository.existsById(userId)) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("Selected user does not exist.")
                    .addConstraintViolation();
            return false;
        }

        return !customerRepository.existsByUserId(userId);
    }
}
