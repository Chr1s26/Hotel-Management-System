package com.project.HotelManagementSystem.entity;

import com.project.HotelManagementSystem.entity.constants.StatusType;

import java.time.LocalDateTime;

public interface SoftDeletable {
    Long getId();
    boolean isDeleted();
    void setDeleted(boolean deleted);
    void setDeletedAt(LocalDateTime deletedAt);
    void setStatus(StatusType status);
}