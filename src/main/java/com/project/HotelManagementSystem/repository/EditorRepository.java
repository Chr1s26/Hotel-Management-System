package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Editor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EditorRepository extends JpaRepository<Editor,Long> {
    Optional<Editor> findByNameIgnoreCase(String name);
    Optional<Editor> findByNameIgnoreCaseAndIdNot(String name, Long id);
}
