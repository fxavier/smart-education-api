package com.xavier.smarteducationapi.common.domain.exception;

/**
 * Base class for all domain-related exceptions.
 * 
 * Domain exceptions represent business rule violations or invalid domain states.
 * They should be used when domain invariants are broken or business logic fails.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public abstract class DomainException extends SmartEducationException {
    
    protected DomainException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    protected DomainException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    protected DomainException(String errorCode, String message, String correlationId) {
        super(errorCode, message, correlationId);
    }
    
    protected DomainException(String errorCode, String message, Throwable cause, String correlationId) {
        super(errorCode, message, cause, correlationId);
    }
}
