package com.hms.constants;

/**
 * Application-wide constants
 */
public class AppConstants {
    
    private AppConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }

    // API Versioning
    public static final String API_V1 = "/api/v1";
    public static final String API_V2 = "/api/v2";
    public static final String API_BASE_PATH = "/hms";
    
    // Cache Names
    public static final String CACHE_PATIENTS = "patients";
    public static final String CACHE_DOCTORS = "doctors";
    public static final String CACHE_APPOINTMENTS = "appointments";
    public static final String CACHE_BILLINGS = "billings";
    public static final long CACHE_DEFAULT_TTL = 3600; // 1 hour in seconds
    
    // Security
    public static final String JWT_SECRET_KEY = "hms-jwt-secret-key-for-signing-tokens-must-be-at-least-256-bits-long";
    public static final long JWT_EXPIRATION = 86400000; // 24 hours in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    
    // Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_DOCTOR = "DOCTOR";
    public static final String ROLE_RECEPTIONIST = "RECEPTIONIST";
    public static final String ROLE_PATIENT = "PATIENT";
    
    // HTTP Response Status Messages
    public static final String SUCCESS = "Success";
    public static final String FAILURE = "Failure";
    
    // Pagination
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    
    // Date Formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    // Entity Status
    public enum EntityStatus {
        ACTIVE("ACTIVE"),
        INACTIVE("INACTIVE"),
        DELETED("DELETED");
        
        private final String value;
        
        EntityStatus(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}
