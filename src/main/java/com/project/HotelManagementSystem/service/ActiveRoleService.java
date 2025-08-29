package com.project.HotelManagementSystem.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service("activeRoleService")
public class ActiveRoleService {

    public boolean hasActiveRole(String requiredRole){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return false;
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = requestAttributes.getRequest().getSession(false);

        if(session == null){
            return false;
        }

        String activeRole = (String) session.getAttribute("activeRole");
        boolean flag = activeRole != null && activeRole.equalsIgnoreCase(requiredRole);
        return activeRole != null && activeRole.equalsIgnoreCase(requiredRole);
    }

    public String getActiveRole() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) return null;

        HttpSession session = requestAttributes.getRequest().getSession(false);
        if (session == null) return null;

        String activeRole = (String) session.getAttribute("activeRole");
        if (activeRole == null) return null;

        return activeRole.startsWith("ROLE_") ? activeRole.substring(5) : activeRole;
    }

}
