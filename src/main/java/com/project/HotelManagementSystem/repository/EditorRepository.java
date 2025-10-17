package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Editor;
import com.project.HotelManagementSystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EditorRepository extends JpaRepository<Editor,Long> {
    Optional<Editor> findByNameIgnoreCase(String name);
    Optional<Editor> findByNameIgnoreCaseAndIdNot(String name, Long id);
    Optional<Editor> findEditorByUser(User user);
    Editor findByUser(User user);

    Page<Editor> findAll(Specification<Editor> spec, Pageable pageable);
}
