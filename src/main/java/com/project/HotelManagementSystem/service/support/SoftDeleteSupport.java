package com.project.HotelManagementSystem.service.support;

import com.project.HotelManagementSystem.entity.SoftDeletable;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.RelationshipInUseException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public final class SoftDeleteSupport {

    private SoftDeleteSupport() {}

    public static <T extends SoftDeletable> void softDelete(
            JpaRepository<T, Long> repository, Long id, String label) {

        T entity = repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(label, id, "id", label,
                        label + " with the id cannot be found"));

        entity.setDeleted(true);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setStatus(StatusType.DELETED);
        repository.save(entity);
    }

    public static <T> void hardDelete(
            JpaRepository<T, Long> repository, Long id, String label) {

        T entity = repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(label, id, "id", label,
                        label + " with the id cannot be found"));

        try {
            repository.delete(entity);
            repository.flush(); // force FK constraints to fire now so we can catch them
        } catch (DataIntegrityViolationException ex) {
            throw new RelationshipInUseException(
                    "Cannot permanently delete this " + label
                            + " because other records still depend on it. "
                            + "Delete or reassign those first, or use soft delete instead.");
        }
    }
}
