package com.project.HotelManagementSystem.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoResponse {
    private Long id;
    private String jwtToken;
    private String name;
    private List<String> roles;

    public UserInfoResponse(Long id, String name, List<String> roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }
}
