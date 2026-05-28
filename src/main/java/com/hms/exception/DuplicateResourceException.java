package com.hms.exception;

/**
 * Exception thrown when attempting to create a duplicate resource
 */
public class DuplicateResourceException extends BaseException {
    
    public DuplicateResourceException(String resourceName, String field, String value) {
        super("DUPLICATE_RESOURCE", 
              String.format("%s with %s '%s' already exists", resourceName, field, value));
    }

    public DuplicateResourceException(String message) {
        super("DUPLICATE_RESOURCE", message);
    }
}
