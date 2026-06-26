package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.user.UserCreateDTO;
import com.project.HotelManagementSystem.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AbstractService extends UserDetailsService {
    User registerNewUser(UserCreateDTO user);
}
