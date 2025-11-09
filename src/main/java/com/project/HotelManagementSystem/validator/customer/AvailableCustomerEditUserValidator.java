package com.project.HotelManagementSystem.validator.customer;

import com.project.HotelManagementSystem.dto.customer.CustomerUpdateDTO;
import com.project.HotelManagementSystem.repository.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AvailableCustomerEditUserValidator implements ConstraintValidator<AvailableCustomerEditUser, CustomerUpdateDTO> {
    private final CustomerRepository customerRepository;

    @Override
    public boolean isValid(CustomerUpdateDTO dto, ConstraintValidatorContext ctx) {
        if (dto.getId() == null || dto.getUser() == null) return true;
        boolean takenByAnother = customerRepository.existsByUserIdAndIdNot(dto.getUser(), dto.getId());
        if (takenByAnother) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("userId").addConstraintViolation();
        }
        return !takenByAnother;
    }
}
