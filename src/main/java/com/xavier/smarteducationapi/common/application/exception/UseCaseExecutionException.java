package com.xavier.smarteducationapi.common.application.exception;

/**
 * Exception thrown when a use case execution fails.
 * 
 * This exception wraps failures that occur during use case execution,
 * including domain exceptions, validation errors, or other application logic failures.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class UseCaseExecutionException extends ApplicationException {
    
    private static final String ERROR_CODE = "USE_CASE_EXECUTION_FAILED";
    
    private final String useCaseName;
    private final Object input;
    
    public UseCaseExecutionException(String useCaseName, String message) {
        super(ERROR_CODE, String.format("Use case '%s' execution failed: %s", useCaseName, message));
        this.useCaseName = useCaseName;
        this.input = null;
    }
    
    public UseCaseExecutionException(String useCaseName, String message, Throwable cause) {
        super(ERROR_CODE, String.format("Use case '%s' execution failed: %s", useCaseName, message), cause);
        this.useCaseName = useCaseName;
        this.input = null;
    }
    
    public UseCaseExecutionException(String useCaseName, Object input, String message, Throwable cause) {
        super(ERROR_CODE, String.format("Use case '%s' execution failed with input '%s': %s", 
                useCaseName, input, message), cause);
        this.useCaseName = useCaseName;
        this.input = input;
    }
    
    public String getUseCaseName() {
        return useCaseName;
    }
    
    public Object getInput() {
        return input;
    }
    
    /**
     * Convenience method to create exception for a specific use case class.
     */
    public static UseCaseExecutionException forUseCase(Class<?> useCaseClass, String message) {
        return new UseCaseExecutionException(useCaseClass.getSimpleName(), message);
    }
    
    /**
     * Convenience method to create exception for a specific use case class with cause.
     */
    public static UseCaseExecutionException forUseCase(Class<?> useCaseClass, String message, Throwable cause) {
        return new UseCaseExecutionException(useCaseClass.getSimpleName(), message, cause);
    }
}
