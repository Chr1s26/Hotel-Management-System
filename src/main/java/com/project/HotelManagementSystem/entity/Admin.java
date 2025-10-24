package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.converter.AdminTypeConverter;
import com.project.HotelManagementSystem.entity.constants.AdminType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admins")
public class Admin extends UserMasterData{

    @Column(unique = true, nullable = true)
    private String passportNumber;

    @Column(unique = false, nullable = true)
    private String nationalIdNumber;

    @Column(nullable = false)
    @Convert(converter = AdminTypeConverter.class)
    private AdminType adminType;

    @OneToOne
    @JoinColumn(name = "app_user_id", unique = true)
    private User user;
}
