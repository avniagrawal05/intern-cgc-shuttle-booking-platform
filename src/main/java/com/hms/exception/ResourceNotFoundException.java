package com.hms.exception;

/**
 * Exception thrown when a requested resource is not found
 */
public class ResourceNotFoundException extends BaseException {
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super("RESOURCE_NOT_FOUND", 
              String.format("%s with id '%s' not found", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String field, String value) {
        super("RESOURCE_NOT_FOUND", 
              String.format("%s with %s '%s' not found", resourceName, field, value));
    }

    public ResourceNotFoundException(String message) {
        super("RESOURCE_NOT_FOUND", message);
    }
}
