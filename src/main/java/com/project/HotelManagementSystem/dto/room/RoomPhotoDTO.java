package com.project.HotelManagementSystem.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomPhotoDTO {
    private Long attachmentId;
    private String url;
}
