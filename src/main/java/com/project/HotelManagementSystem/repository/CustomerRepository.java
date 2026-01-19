package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Customer;
import com.project.HotelManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    boolean existsByNameIgnoreCaseAndPhone(String name, String phone);
    boolean existsByNameIgnoreCaseAndPhoneAndIdNot(String name , String phone, Long id);

    boolean existsByUserIdAndIdNot(Long userId, Long id);
    boolean existsByUserId(Long userId);

    Optional<Customer> findByNameIgnoreCaseAndPhone(String name, String phone);

    Optional<Customer> findByNameIgnoreCaseAndPhoneAndIdNot(String name, String phone, Long id);

    Optional<Customer> findByUser(User currentUser);
}
