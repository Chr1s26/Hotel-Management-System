//package com.project.HotelManagementSystem.util;
//
//import com.project.HotelManagementSystem.entity.User;
//import com.project.HotelManagementSystem.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AuthUtil {
//
//    @Autowired
//    UserRepository userRepository;
//
//    public String loggedInEmail(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByNameIgnoreCase(authentication.getName())
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
//
//        return user.getEmail();
//    }
//
//    public Long loggedInUserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByNameIgnoreCase(authentication.getName())
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
//        return user.getId();
//    }
//
//
//    public User loggedInUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        User user = userRepository.findByNameIgnoreCase(authentication.getName())
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
//        return user;
//    }
//}
