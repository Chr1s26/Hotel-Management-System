package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.user.UserCreateDTO;
import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.AccountNotConfirmedException;
import com.project.HotelManagementSystem.exception.InvalidRoleException;
import com.project.HotelManagementSystem.exception.UserNameNotFoundException;
import com.project.HotelManagementSystem.repository.RoleRepository;
import com.project.HotelManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements AbstractService{
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String parameter)  {
        User user = userRepository.findByEmail(parameter)
                .orElseGet(() -> userRepository.findByNameIgnoreCase(parameter)
                        .orElseThrow(() -> new UserNameNotFoundException("Username is not found","/login")));

//        if(user.getConfirmedAt() == null){
//            throw new AccountNotConfirmedException("Account not found");
//        }
        return UserDetailsImpl.build(user);
    }

    @Override
    public User registerNewUser(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setName(userCreateDTO.getName());
        user.setEmail(userCreateDTO.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(StatusType.ACTIVE);
        Role userRole = roleRepository.findByRoleName("NORMAL_USER").orElseThrow(() -> new InvalidRoleException("user",userCreateDTO,"role","/register","User Role cannot assign"));
        user.setRoles(Collections.singleton(userRole));
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        return userRepository.save(user);
    }
}
