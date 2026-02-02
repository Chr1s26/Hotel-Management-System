package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpService otpService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;

        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetailsImpl userDetails) {
            return userRepository.findById(userDetails.getId())
                    .orElse(null);
        }

        return null;
    }

    public String getCurrentUserRole(){
        ServletRequestAttributes attrs =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attrs == null) return null;
        HttpSession session = attrs.getRequest().getSession(false);
        if(session == null) return null;
        return (String) session.getAttribute("activeRole");
    }

    //checking is user admin before doing export. cannot use above method because async no requestcontextholder is null
    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getAuthorities() == null) {
            return false;
        }

        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
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
