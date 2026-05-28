package com.hms.exception;

/**
 * Exception thrown for business logic violations
 */
public class BusinessException extends BaseException {
    
    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(String message) {
        super("BUSINESS_ERROR", message);
    }
}
