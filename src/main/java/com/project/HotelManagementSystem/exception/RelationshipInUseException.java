package com.project.HotelManagementSystem.exception;

public class RelationshipInUseException extends RuntimeException {
    public RelationshipInUseException(String message) {
        super(message);
    }
}
