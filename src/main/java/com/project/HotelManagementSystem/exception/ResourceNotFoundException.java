package com.project.HotelManagementSystem.exception;

public class ResourceNotFoundException extends RuntimeException {

    String resourceName;
    String field;
    String fieldName;
    Long fieldId;

    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s : %s",resourceName,field,fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s : %d",resourceName,field,fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }

    @Override
    public String getMessage() {
        if (fieldName != null)
            return String.format("%s not found with %s : %s", resourceName, field, fieldName);
        else
            return String.format("%s not found with %s : %d", resourceName, field, fieldId);
    }
}
