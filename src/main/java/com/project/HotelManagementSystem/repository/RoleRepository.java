package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByRoleName(String user);
    Optional<Role> findByRoleNameAndIdNot(String roleName, Long id);
}