package com.project.HotelManagementSystem.entity.security;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    @Autowired
    private UserRepository repo;
    @Autowired
    private AuthService auth;

    public boolean isOwner(Long id) {
        User entity = repo.findById(id).orElse(null);
        return entity != null &&
                entity.getCreatedBy().getId().equals(auth.getCurrentUser().getId());
    }
}
