package com.project.HotelManagementSystem.validator.editor;

import com.project.HotelManagementSystem.dto.editor.EditorUpdateDTO;
import com.project.HotelManagementSystem.repository.EditorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AvailableEditorEditUserValidator implements ConstraintValidator<AvailableEditorEditUser, EditorUpdateDTO> {
    private final EditorRepository editorRepository;

    @Override
    public boolean isValid(EditorUpdateDTO dto, ConstraintValidatorContext ctx) {
        if (dto.getId() == null || dto.getUser() == null) return true;

        boolean takenByAnother = editorRepository.existsByUserIdAndIdNot(dto.getUser(), dto.getId());
        if (takenByAnother) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("userId").addConstraintViolation();
        }
        return !takenByAnother;
    }
}
