package com.xavier.smarteducationapi.common.domain.exception;

/**
 * Exception thrown when a requested entity cannot be found.
 * 
 * This exception is typically thrown by repositories or domain services
 * when trying to retrieve an entity by its identifier but the entity doesn't exist.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class EntityNotFoundException extends DomainException {
    
    private static final String ERROR_CODE = "ENTITY_NOT_FOUND";
    
    private final String entityType;
    private final Object entityId;
    
    public EntityNotFoundException(String entityType, Object entityId) {
        super(ERROR_CODE, String.format("Entity of type '%s' with id '%s' not found", entityType, entityId));
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    public EntityNotFoundException(String entityType, Object entityId, String additionalInfo) {
        super(ERROR_CODE, String.format("Entity of type '%s' with id '%s' not found. %s", 
                entityType, entityId, additionalInfo));
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    public EntityNotFoundException(String entityType, Object entityId, Throwable cause) {
        super(ERROR_CODE, String.format("Entity of type '%s' with id '%s' not found", entityType, entityId), cause);
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public Object getEntityId() {
        return entityId;
    }
    
    /**
     * Convenience method to create exception for a specific entity class.
     */
    public static EntityNotFoundException forEntity(Class<?> entityClass, Object id) {
        return new EntityNotFoundException(entityClass.getSimpleName(), id);
    }
}
