package com.project.HotelManagementSystem.validator;

import com.project.HotelManagementSystem.repository.AdminRepository;
import com.project.HotelManagementSystem.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AvailableAdminEditUserValidator implements ConstraintValidator<AvailableAdminEditUser, Long> {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext ctx) {
        if (userId == null) return false;

        if (!userRepository.existsById(userId)) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("Selected user does not exist.")
                    .addConstraintViolation();
            return false;
        }
        //role remove lote ynn

        return true;
    }
}
