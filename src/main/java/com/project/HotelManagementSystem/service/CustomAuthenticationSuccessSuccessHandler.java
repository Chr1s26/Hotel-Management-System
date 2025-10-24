package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String parameter = authentication.getName();

        User appUser = userRepository.findByEmail(parameter)
                .orElseGet(() -> userRepository.findByNameIgnoreCase(parameter)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));

        if (appUser != null) {
            request.getSession().setAttribute("currentUser", appUser);
        }

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        HttpSession session = request.getSession();

        if(roles.size() > 1){
            request.getSession().setAttribute("userRoles", new ArrayList<>(roles));
            response.sendRedirect("/select-role");
        }else if(roles.contains("ROLE_ADMIN")){
            session.setAttribute("userRoles", new ArrayList<>(roles));
            session.setAttribute("activeRole", "ROLE_ADMIN");
            response.sendRedirect("/home");
        }else if(roles.contains("ROLE_EDITOR")){
            session.setAttribute("userRoles", new ArrayList<>(roles));
            session.setAttribute("activeRole", "ROLE_EDITOR");
            response.sendRedirect("/home");
        } else if(roles.contains("ROLE_NORMAL_USER")){
            session.setAttribute("userRoles", new ArrayList<>(roles));
            session.setAttribute("activeRole", "ROLE_NORMAL_USER");
            response.sendRedirect("/home");
        } else {
            response.sendRedirect("/");
        }

    }
}
