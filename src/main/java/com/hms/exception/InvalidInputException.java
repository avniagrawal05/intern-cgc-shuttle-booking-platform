package com.hms.exception;

/**
 * Exception thrown when invalid input is provided
 */
public class InvalidInputException extends BaseException {
    
    public InvalidInputException(String field, String message) {
        super("INVALID_INPUT", String.format("Invalid %s: %s", field, message));
    }

    public InvalidInputException(String message) {
        super("INVALID_INPUT", message);
    }
}
