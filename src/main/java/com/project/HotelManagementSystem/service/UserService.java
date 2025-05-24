package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    public User updateUser(Long id,User user) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User updatedUser = optionalUser.get();
            updatedUser.setName(user.getName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setPhone(user.getPhone());
            updatedUser.setUserRole(user.getUserRole());
            if(user.getDateOfBirth() != null){
                updatedUser.setDateOfBirth(user.getDateOfBirth());
            }
            updatedUser.setNationality(user.getNationality());
            updatedUser.setPoint(user.getPoint());
            return this.userRepository.save(updatedUser);
        }
        return null;
    }

    public void deleteUser( Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent()) {
            this.userRepository.deleteById(id);
        }
    }

    public User findUserById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }
}
