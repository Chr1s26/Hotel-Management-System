package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface AbstractService extends UserDetailsService {
    User registerNewUser(User user);
}
