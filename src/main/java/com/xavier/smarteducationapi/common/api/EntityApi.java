package com.xavier.smarteducationapi.common.api;

import com.xavier.smarteducationapi.common.domain.entity.AggregateRoot;
import com.xavier.smarteducationapi.common.domain.entity.BaseEntity;
import com.xavier.smarteducationapi.common.domain.valueobject.BaseId;

/**
 * API facade for entity and aggregate root operations.
 * 
 * This interface provides access to base entity types and utility methods
 * that other modules can use when working with domain entities.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-15
 */
public interface EntityApi {
    
    /**
     * Gets the base entity class that all entities should extend.
     * 
     * @return the base entity class
     */
    Class<BaseEntity<?>> getBaseEntityClass();
    
    /**
     * Gets the aggregate root class that aggregates should extend.
     * 
     * @return the aggregate root class
     */
    Class<AggregateRoot<?>> getAggregateRootClass();
    
    /**
     * Gets the base identifier class for typed IDs.
     * 
     * @return the base ID class
     */
    Class<BaseId<?>> getBaseIdClass();
    
}
