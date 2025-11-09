package com.project.HotelManagementSystem.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelPhotoDTO {
    private Long attachmentId;
    private String url;
}
