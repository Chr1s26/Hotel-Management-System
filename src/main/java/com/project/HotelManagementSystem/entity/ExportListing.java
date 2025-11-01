package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.entity.constants.FileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExportListing extends MasterData{
    @Column
    private String fileName;
    @Column
    private FileType fileType;
}
