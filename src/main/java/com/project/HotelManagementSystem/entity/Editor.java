package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.converter.EditorTypeConverter;
import com.project.HotelManagementSystem.entity.constants.EditorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "editors")
public class Editor extends UserMasterData{

    @Column(unique = true, nullable = true)
    private String passportNumber;

    @Column(unique = false, nullable = true)
    private String nationalIdNumber;

    @Column(nullable = false)
    @Convert(converter = EditorTypeConverter.class)
    private EditorType editorType;

    @Column(nullable = true)
    private boolean approvedByAdmin;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToOne
    @JoinColumn(name = "app_user_id", unique = true)
    private User user;
}
