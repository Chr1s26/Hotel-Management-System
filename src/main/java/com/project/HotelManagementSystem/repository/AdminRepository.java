package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Admin;
import com.project.HotelManagementSystem.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin> {
    Optional<Admin> findByNameIgnoreCase(String name);
    Optional<Admin> findByNameIgnoreCaseAndIdNot(String name, Long id);
    Optional<Admin> findByUser(User user);
    Page<Admin> findAll(Specification<Admin> spec, Pageable pageable);
    boolean existsByUser_Id(Long userId);

    boolean existsByPassportNumberIgnoreCaseAndIdNot(String passportNumber, Long id);
    boolean existsByNationalIdNumberIgnoreCaseAndIdNot(String nationalIdNumber, Long id);
    boolean existsByPassportNumberIgnoreCase(String passportNumber);
    boolean existsByNationalIdNumberIgnoreCase(String nationalIdNumber);

    boolean existsByUserIdAndIdNot(Long user, Long id);

    Optional<Admin> findByPhone(@NotBlank(message = "Phone number cannot be empty.") @Pattern(regexp = "^[0-9\\-\\s()]{8,15}$", message = "Invalid phone number format") String phone);
}
