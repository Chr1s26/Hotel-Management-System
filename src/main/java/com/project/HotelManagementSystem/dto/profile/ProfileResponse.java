package com.project.HotelManagementSystem.dto.profile;

import com.project.HotelManagementSystem.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse<T> {
    private T object;
    private UserDTO userDTO;
}
