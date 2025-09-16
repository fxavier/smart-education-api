package com.xavier.smarteducationapi.common.infrastructure.exception;

/**
 * Exception thrown when external service calls fail.
 * 
 * This includes HTTP client errors, service timeouts, authentication failures,
 * and other issues when communicating with external systems.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class ExternalServiceException extends InfrastructureException {
    
    private static final String ERROR_CODE = "EXTERNAL_SERVICE_ERROR";
    
    private final String serviceName;
    private final String operation;
    private final Integer statusCode;
    
    public ExternalServiceException(String serviceName, String message) {
        super(ERROR_CODE, String.format("External service '%s' error: %s", serviceName, message));
        this.serviceName = serviceName;
        this.operation = null;
        this.statusCode = null;
    }
    
    public ExternalServiceException(String serviceName, String message, Throwable cause) {
        super(ERROR_CODE, String.format("External service '%s' error: %s", serviceName, message), cause);
        this.serviceName = serviceName;
        this.operation = null;
        this.statusCode = null;
    }
    
    public ExternalServiceException(String serviceName, String operation, Integer statusCode, String message) {
        super(ERROR_CODE, String.format("External service '%s' operation '%s' failed with status %d: %s", 
                serviceName, operation, statusCode, message));
        this.serviceName = serviceName;
        this.operation = operation;
        this.statusCode = statusCode;
    }
    
    public ExternalServiceException(String serviceName, String operation, Integer statusCode, String message, Throwable cause) {
        super(ERROR_CODE, String.format("External service '%s' operation '%s' failed with status %d: %s", 
                serviceName, operation, statusCode, message), cause);
        this.serviceName = serviceName;
        this.operation = operation;
        this.statusCode = statusCode;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public Integer getStatusCode() {
        return statusCode;
    }
    
    /**
     * Convenience method for timeout errors.
     */
    public static ExternalServiceException timeout(String serviceName, String operation, Throwable cause) {
        return new ExternalServiceException(serviceName, operation, 408, "Request timeout", cause);
    }
    
    /**
     * Convenience method for authentication failures.
     */
    public static ExternalServiceException authenticationFailed(String serviceName, String operation) {
        return new ExternalServiceException(serviceName, operation, 401, "Authentication failed");
    }
    
    /**
     * Convenience method for service unavailable.
     */
    public static ExternalServiceException serviceUnavailable(String serviceName, Throwable cause) {
        return new ExternalServiceException(serviceName, "Service unavailable", cause);
    }
}
