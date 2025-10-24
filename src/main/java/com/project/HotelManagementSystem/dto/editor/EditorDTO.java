package com.project.HotelManagementSystem.dto.editor;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.EditorType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditorDTO {
    private Long id;
    private String name;
    private String phone;
    private LocalDate dateOfBirth;
    private String nationality;
    private String passportNumber;
    private String nationalIdNumber;
    private EditorType editorType;
    private User user;
    private StatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User createdBy;
    private User updatedBy;
}
