package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.InvalidRoleException;
import com.project.HotelManagementSystem.repository.RoleRepository;
import com.project.HotelManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements AbstractService{
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthService authService;

    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String parameter) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(parameter)
                .orElseGet(() -> userRepository.findByNameIgnoreCase(parameter)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));
        return UserDetailsImpl.build(user);
    }

    @Override
    public User registerNewUser(User user) {
        if(userRepository.existsByNameIgnoreCase(user.getName())){
            throw new RuntimeException("Username already exists");
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(StatusType.ACTIVE);
        Role userRole = roleRepository.findByRoleName("NORMAL_USER").orElseThrow(() -> new InvalidRoleException("Role not found"));
        user.setRoles(Collections.singleton(userRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmedAt(LocalDateTime.now());
        user.setCreatedBy(authService.getCurrentUser());
        return userRepository.save(user);
    }
}
