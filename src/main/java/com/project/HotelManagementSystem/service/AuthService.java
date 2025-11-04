package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpService otpService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getCurrentUser(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);
        return session != null ? (User) session.getAttribute("currentUser") : null;
    }

    public void resetPassword(String email, String password) {
        Optional<User> userOp = userRepository.findByEmail(email);
        User user = userOp.get();
        if (userOp.isPresent()) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }else{
            throw new ResourceNotFoundException("user",email,"email","forget-password","User with email ( "+email+" ) doesn't exist");
        }
    }
}
