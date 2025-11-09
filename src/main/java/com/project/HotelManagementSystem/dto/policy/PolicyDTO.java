package com.project.HotelManagementSystem.dto.policy;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyDTO {
    private Long id;
    private String title;
    private String description;
    private String applicableTo;
    private StatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User createdBy;
    private User updatedBy;
}
