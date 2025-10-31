package com.project.HotelManagementSystem.dto.editor;

import com.project.HotelManagementSystem.entity.constants.EditorType;
import com.project.HotelManagementSystem.validator.NumericString;
import com.project.HotelManagementSystem.validator.editor.AvailableEditorEditUser;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@AvailableEditorEditUser
public class EditorUpdateDTO {
    private Long id;
    @NotBlank(message = "Name cannot be empty.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name cannot include numbers or symbols")
    private String name;
    @NotBlank(message = "Phone number cannot be empty.")
    @Pattern(regexp = "^[0-9\\-\\s()]{8,15}$", message = "Invalid phone number format")
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date of Birth cannot be empty.")
    @PastOrPresent(message = "Birth date cannot be in the future")
    private LocalDate dateOfBirth;
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Nationality cannot include numbers or symbols")
    private String nationality;
    @Pattern(
            regexp = "^[A-Za-z0-9]{6,9}$|^$",
            message = "Invalid passport number format (6–9 letters or digits only)"
    )
    private String passportNumber;
    @Pattern(
            regexp = "^[A-Za-z0-9/\\-()]+$|^$",
            message = "Invalid ID format (only A–Z, 0–9, /, -, (, ) are allowed)"
    )
    private String nationalIdNumber;
    @NotNull(message = "Admin type cannot be empty")
    private EditorType editorType;
    private Long user;
}
