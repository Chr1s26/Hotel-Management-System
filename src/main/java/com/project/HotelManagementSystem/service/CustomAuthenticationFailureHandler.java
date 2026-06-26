package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.exception.AccountNotConfirmedException;
import com.project.HotelManagementSystem.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private UserRepository userRepository;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        String name = request.getParameter("username");
//
//        if(exception.getCause() instanceof AccountNotConfirmedException) {
//            User user = userRepository.findByNameIgnoreCase(name).orElse(null);
//            if(user != null) {
//                response.sendRedirect("/confirm-account/otp?email=" + user.getEmail() + "&error=unconfirmed&message=" + "Please Verify the OTP!!");
//                return;
//            }
//        }
        response.sendRedirect("/login?error=true");
    }
}
