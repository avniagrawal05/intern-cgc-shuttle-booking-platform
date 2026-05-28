package com.hms.constants;

/**
 * HTTP Response related constants
 */
public class HttpResponseConstants {
    
    private HttpResponseConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }

    // Response Codes
    public static final int SUCCESS_CODE = 200;
    public static final int CREATED_CODE = 201;
    public static final int BAD_REQUEST_CODE = 400;
    public static final int UNAUTHORIZED_CODE = 401;
    public static final int FORBIDDEN_CODE = 403;
    public static final int NOT_FOUND_CODE = 404;
    public static final int CONFLICT_CODE = 409;
    public static final int INTERNAL_SERVER_ERROR_CODE = 500;

    // Error Messages
    public static final String INVALID_INPUT_MESSAGE = "Invalid input provided";
    public static final String RESOURCE_NOT_FOUND_MESSAGE = "Resource not found";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error occurred";
    public static final String UNAUTHORIZED_MESSAGE = "Unauthorized access";
}
