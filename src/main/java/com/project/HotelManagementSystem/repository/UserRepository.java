package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByNameIgnoreCase(@NotBlank(message = "Name cannot be empty.") String name);

    Optional<User> findByEmail(String email);
    
    boolean existsByName(String name);

    boolean existsByEmail(String email);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    Page<User> findAll(Specification<User> spec, Pageable pageable);
}
