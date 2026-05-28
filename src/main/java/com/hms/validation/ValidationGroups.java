package com.hms.validation;

/**
 * Validation groups for bean validation
 * Used to differentiate between create and update operations
 */
public final class ValidationGroups {
    
    private ValidationGroups() {
        throw new AssertionError("Cannot instantiate validation groups class");
    }
    
    /**
     * Validation group for create operations
     * Fields marked with this group are validated only during creation
     */
    public interface OnCreate {}
    
    /**
     * Validation group for update operations
     * Fields marked with this group are validated only during update
     */
    public interface OnUpdate {}
    
    /**
     * Validation group for patch operations
     * Fields marked with this group are validated during partial updates
     */
    public interface OnPatch {}
}
