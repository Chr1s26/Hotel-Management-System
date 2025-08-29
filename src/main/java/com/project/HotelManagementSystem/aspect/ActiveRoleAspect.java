package com.project.HotelManagementSystem.aspect;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.service.ActiveRoleService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.security.access.AccessDeniedException;
import java.util.Arrays;

@Aspect
@Component
public class ActiveRoleAspect {

    private final ActiveRoleService activeRoleService;

    public ActiveRoleAspect(ActiveRoleService activeRoleService) {
        this.activeRoleService = activeRoleService;
    }

    @Before("@within(activeRole) || @annotation(activeRole)")
    public void checkActiveRole(ActiveRole activeRole) throws AccessDeniedException {
        String[] requiredRoles = activeRole.value();

        boolean hasRole = Arrays.stream(requiredRoles)
                .anyMatch(r -> {
                    String activeRoles = activeRoleService.getActiveRole(); 
                    if(activeRoles == null) return false;
                    String normalized = activeRoles.startsWith("ROLE_") ? activeRoles.substring(5) : activeRoles;
                    return normalized.equalsIgnoreCase(r);
                });

        if(!hasRole){
            throw new AccessDeniedException("Access denied : Active role must be " + Arrays.toString(requiredRoles));
        }
    }

}
