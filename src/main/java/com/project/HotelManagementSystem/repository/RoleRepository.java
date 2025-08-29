package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(String user);
    Optional<Role> findByRoleNameAndIdNot(String roleName, Long id);
}