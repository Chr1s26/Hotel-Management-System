package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.UserRole;
import com.project.HotelManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;

@Service
public class UserDetailsServiceImpl implements AbstractService{
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByNameIgnoreCase(name).orElseThrow(() -> new UsernameNotFoundException("User not found"));
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
        if(userRepository.existsByPhone(user.getPhone())){
            throw new RuntimeException("Phone already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.CUSTOMER);
        return userRepository.save(user);
    }
}
