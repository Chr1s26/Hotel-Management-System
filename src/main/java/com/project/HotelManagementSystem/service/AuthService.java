package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);
        return session != null ? (User) session.getAttribute("currentUser") : null;
    }
}
