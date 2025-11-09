package com.project.HotelManagementSystem.dto.customer;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private Long id;
    private String name;
    private String phone;
    private LocalDate dateOfBirth;
    private String nationality;
    private User user;
    private boolean vipStatus;
    private StatusType status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private User createdBy;
    private User updatedBy;
}
