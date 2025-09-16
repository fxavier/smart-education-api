package com.xavier.smarteducationapi.common.infrastructure.exception;

import com.xavier.smarteducationapi.common.domain.exception.SmartEducationException;

/**
 * Base class for all infrastructure-layer exceptions.
 * 
 * Infrastructure exceptions represent technical failures such as database
 * connection issues, external service failures, messaging problems, etc.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public abstract class InfrastructureException extends SmartEducationException {
    
    protected InfrastructureException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    protected InfrastructureException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    protected InfrastructureException(String errorCode, String message, String correlationId) {
        super(errorCode, message, correlationId);
    }
    
    protected InfrastructureException(String errorCode, String message, Throwable cause, String correlationId) {
        super(errorCode, message, cause, correlationId);
    }
}
