package com.xavier.smarteducationapi.common.application.exception;

import com.xavier.smarteducationapi.common.domain.exception.SmartEducationException;

/**
 * Base class for all application-layer exceptions.
 * 
 * Application exceptions represent failures in use cases, application services,
 * or other application-layer components. They typically wrap domain exceptions
 * or represent application-specific error conditions.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public abstract class ApplicationException extends SmartEducationException {
    
    protected ApplicationException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    protected ApplicationException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    protected ApplicationException(String errorCode, String message, String correlationId) {
        super(errorCode, message, correlationId);
    }
    
    protected ApplicationException(String errorCode, String message, Throwable cause, String correlationId) {
        super(errorCode, message, cause, correlationId);
    }
}
