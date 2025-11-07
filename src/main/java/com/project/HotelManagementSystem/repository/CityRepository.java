package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long>, JpaSpecificationExecutor<City> {
    Optional<City> findByNameIgnoreCase(String name);
    Optional<City> findByNameIgnoreCaseAndRegion(String name, Region region);
    Optional<City> findByNameIgnoreCaseAndIdNot(String name, Long id);
    Optional<City> findByNameIgnoreCaseAndRegionAndIdNot(String name, Region region, Long id);
    List<City> findAll(Specification<City> spec);
    Page<City> findAll(Specification<City> spec, Pageable pageable);
}
