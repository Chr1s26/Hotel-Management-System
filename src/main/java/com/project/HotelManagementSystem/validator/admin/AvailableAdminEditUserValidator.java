package com.project.HotelManagementSystem.validator.admin;

import com.project.HotelManagementSystem.dto.admin.AdminUpdateDTO;
import com.project.HotelManagementSystem.repository.AdminRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AvailableAdminEditUserValidator implements ConstraintValidator<AvailableAdminEditUser, AdminUpdateDTO> {

    private final AdminRepository adminRepository;

    @Override
    public boolean isValid(AdminUpdateDTO dto, ConstraintValidatorContext ctx) {
        if (dto.getId() == null || dto.getUser() == null) return true;

        boolean takenByAnother = adminRepository.existsByUserIdAndIdNot(dto.getUser(), dto.getId());
        if (takenByAnother) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("userId").addConstraintViolation();
        }
        return !takenByAnother;
    }
}
