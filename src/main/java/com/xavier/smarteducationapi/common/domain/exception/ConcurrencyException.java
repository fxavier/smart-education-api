package com.xavier.smarteducationapi.common.domain.exception;

/**
 * Exception thrown when a concurrency conflict occurs.
 * 
 * This typically happens during optimistic locking scenarios where
 * the entity has been modified by another transaction since it was loaded.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class ConcurrencyException extends DomainException {
    
    private static final String ERROR_CODE = "CONCURRENCY_CONFLICT";
    
    private final String entityType;
    private final Object entityId;
    private final Long expectedVersion;
    private final Long actualVersion;
    
    public ConcurrencyException(String entityType, Object entityId, Long expectedVersion, Long actualVersion) {
        super(ERROR_CODE, String.format(
                "Concurrency conflict for %s with id '%s'. Expected version: %d, actual version: %d", 
                entityType, entityId, expectedVersion, actualVersion));
        this.entityType = entityType;
        this.entityId = entityId;
        this.expectedVersion = expectedVersion;
        this.actualVersion = actualVersion;
    }
    
    public ConcurrencyException(String message) {
        super(ERROR_CODE, message);
        this.entityType = null;
        this.entityId = null;
        this.expectedVersion = null;
        this.actualVersion = null;
    }
    
    public ConcurrencyException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
        this.entityType = null;
        this.entityId = null;
        this.expectedVersion = null;
        this.actualVersion = null;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public Object getEntityId() {
        return entityId;
    }
    
    public Long getExpectedVersion() {
        return expectedVersion;
    }
    
    public Long getActualVersion() {
        return actualVersion;
    }
    
    /**
     * Convenience method to create exception for a specific entity class.
     */
    public static ConcurrencyException forEntity(Class<?> entityClass, Object id, Long expectedVersion, Long actualVersion) {
        return new ConcurrencyException(entityClass.getSimpleName(), id, expectedVersion, actualVersion);
    }
}
