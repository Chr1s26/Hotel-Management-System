package com.project.HotelManagementSystem.dto.editor;

import com.project.HotelManagementSystem.entity.constants.EditorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditorCreateDTO {
    private Long id;
    private String name;
    private String phone;
    private LocalDate dateOfBirth;
    private String nationality;
    private String passportNumber;
    private String nationalIdNumber;
    private EditorType editorType;
    private Long app_user_id;
}
