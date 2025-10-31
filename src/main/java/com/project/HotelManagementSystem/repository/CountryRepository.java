package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country,Long> {
    Optional<Country> findByNameIgnoreCase(String name);
    Optional<Country> findByNameIgnoreCaseAndIdNot(String name, Long id);
    List<Country> findAll(Specification<Country> spec);
    Page<Country> findAll(Specification<Country> spec, Pageable pageable);
}
