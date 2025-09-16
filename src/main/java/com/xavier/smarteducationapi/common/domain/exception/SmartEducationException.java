package com.xavier.smarteducationapi.common.domain.exception;

import java.time.Instant;
import java.util.UUID;

/**
 * Base exception for all Smart Education API exceptions.
 * 
 * Provides common functionality like error codes, timestamps, and correlation IDs
 * for tracking and debugging purposes.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public abstract class SmartEducationException extends RuntimeException {
    
    private final String errorCode;
    private final Instant timestamp;
    private final String correlationId;
    
    protected SmartEducationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.correlationId = UUID.randomUUID().toString();
    }
    
    protected SmartEducationException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.correlationId = UUID.randomUUID().toString();
    }
    
    protected SmartEducationException(String errorCode, String message, String correlationId) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.correlationId = correlationId;
    }
    
    protected SmartEducationException(String errorCode, String message, Throwable cause, String correlationId) {
        super(message, cause);
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.correlationId = correlationId;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public String getCorrelationId() {
        return correlationId;
    }
    
    @Override
    public String toString() {
        return String.format("%s{errorCode='%s', message='%s', timestamp='%s', correlationId='%s'}",
                getClass().getSimpleName(), errorCode, getMessage(), timestamp, correlationId);
    }
}
