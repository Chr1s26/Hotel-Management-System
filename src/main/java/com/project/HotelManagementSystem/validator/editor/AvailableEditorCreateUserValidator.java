package com.project.HotelManagementSystem.validator.editor;

import com.project.HotelManagementSystem.repository.EditorRepository;
import com.project.HotelManagementSystem.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AvailableEditorCreateUserValidator implements ConstraintValidator<AvailableEditorCreateUser, Long> {

    private final EditorRepository editorRepository;
    private final UserRepository userRepository;

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext ctx) {
        if (userId == null) return true;

        if (!userRepository.existsById(userId)) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("Selected user does not exist.")
                    .addConstraintViolation();
            return false;
        }

        return !editorRepository.existsByUser_Id(userId);
    }
}
