package com.project.HotelManagementSystem.converter;

import com.project.HotelManagementSystem.entity.constants.UserRole;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter extends BaseEnumConverter<UserRole,Integer> {
    public UserRoleConverter(){
        super(UserRole.class);
    }
}
