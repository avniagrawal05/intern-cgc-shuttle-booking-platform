package com.hms.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hms.constants.HttpResponseConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Generic API Response Wrapper for all API responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private int statusCode;
    private String status;
    private String message;
    private T data;
    private List<String> errors;
    private LocalDateTime timestamp;
    private String path;

    /**
     * Success response builder
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .statusCode(HttpResponseConstants.SUCCESS_CODE)
                .status("SUCCESS")
                .message(message != null ? message : "Request processed successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Created response builder
     */
    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .statusCode(HttpResponseConstants.CREATED_CODE)
                .status("CREATED")
                .message(message != null ? message : "Resource created successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Error response builder
     */
    public static <T> ApiResponse<T> error(int statusCode, String status, String message, List<String> errors) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .status(status)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Error response builder with single error message
     */
    public static <T> ApiResponse<T> error(int statusCode, String message) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .status("ERROR")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
