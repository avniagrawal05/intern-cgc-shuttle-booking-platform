package com.hms.config;

import com.hms.constants.HttpResponseConstants;
import com.hms.exception.BaseException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Global Exception Handler for all controllers
 * Handles all exceptions and returns standardized API responses
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle ResourceNotFoundException
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(
            ResourceNotFoundException ex, 
            WebRequest request) {
        log.warn("ResourceNotFoundException occurred: {}", ex.getMessage());
        
        ApiResponse<?> response = ApiResponse.error(
                HttpResponseConstants.NOT_FOUND_CODE,
                "NOT_FOUND",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle MethodArgumentNotValidException (Validation errors)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        log.warn("Validation error occurred");
        
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(String.format("%s: %s", fieldName, errorMessage));
        });

        ApiResponse<?> response = ApiResponse.error(
                HttpResponseConstants.BAD_REQUEST_CODE,
                "VALIDATION_FAILED",
                "Validation error occurred",
                errors
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle BaseException and its subclasses
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleBaseException(
            BaseException ex,
            WebRequest request) {
        log.warn("BaseException occurred: {} - {}", ex.getErrorCode(), ex.getMessage());
        
        int statusCode = ex instanceof ResourceNotFoundException ? 
                HttpResponseConstants.NOT_FOUND_CODE : 
                HttpResponseConstants.BAD_REQUEST_CODE;
        
        ApiResponse<?> response = ApiResponse.error(
                statusCode,
                ex.getErrorCode(),
                ex.getMessage()
        );
        HttpStatus httpStatus = statusCode == HttpResponseConstants.NOT_FOUND_CODE ? 
                HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * Handle MethodArgumentTypeMismatchException
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        log.warn("Type mismatch error: {}", ex.getMessage());
        
        String error = String.format("Parameter '%s' should be of type %s", 
                ex.getName(), ex.getRequiredType().getSimpleName());
        
        ApiResponse<?> response = ApiResponse.error(
                HttpResponseConstants.BAD_REQUEST_CODE,
                "INVALID_PARAMETER_TYPE",
                error
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle 404 Not Found
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoHandlerFound(
            NoHandlerFoundException ex,
            WebRequest request) {
        log.warn("Endpoint not found: {}", ex.getRequestURL());
        
        ApiResponse<?> response = ApiResponse.error(
                HttpResponseConstants.NOT_FOUND_CODE,
                "ENDPOINT_NOT_FOUND",
                String.format("Endpoint '%s' not found", ex.getRequestURL())
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle generic Exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(
            Exception ex,
            WebRequest request) {
        log.error("Unexpected exception occurred", ex);
        
        ApiResponse<?> response = ApiResponse.error(
                HttpResponseConstants.INTERNAL_SERVER_ERROR_CODE,
                "INTERNAL_SERVER_ERROR",
                HttpResponseConstants.INTERNAL_SERVER_ERROR_MESSAGE
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
