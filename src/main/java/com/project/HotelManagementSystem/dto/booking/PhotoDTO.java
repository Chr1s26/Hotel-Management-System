package com.project.HotelManagementSystem.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDTO {
    private Long id;
    private String url;
    private String sourceType;
}
