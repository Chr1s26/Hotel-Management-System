package com.project.HotelManagementSystem.dto.editor;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.EditorType;
import com.project.HotelManagementSystem.validator.AvailableEditorUser;
import com.project.HotelManagementSystem.validator.NumericString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditorUpdateDTO {
    private Long id;
    @NotBlank(message = "Name cannot be empty.")
    private String name;
    @NotBlank(message = "Phone number cannot be empty.")
    @Size(min = 8, message = "Phone number must include at least 8 characters.")
    @NumericString(message = "Phone number cannot be string")
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date of Birth cannot be empty.")
    private LocalDate dateOfBirth;
    @NotBlank(message = "Nationality cannot be empty.")
    private String nationality;
    private String passportNumber;
    private String nationalIdNumber;
    @NotNull(message = "Editor type cannot be empty")
    private EditorType editorType;
    @NotNull(message = "App user is required.")
    @AvailableEditorUser(message = "This user is already assigned to another editor.")
    private Long app_user_id;
}
