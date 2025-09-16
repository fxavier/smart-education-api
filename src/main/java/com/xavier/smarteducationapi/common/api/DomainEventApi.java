package com.xavier.smarteducationapi.common.api;

import java.util.Collection;

import com.xavier.smarteducationapi.common.domain.event.DomainEvent;
import com.xavier.smarteducationapi.common.domain.event.DomainEventPublisher;

/**
 * API facade for domain event operations.
 * 
 * This interface provides a simplified API for other modules to interact
 * with the domain event system without directly depending on internal
 * implementation details.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-15
 */
public interface DomainEventApi {
    
    /**
     * Publishes a single domain event.
     * 
     * @param event the domain event to publish
     */
    void publishEvent(DomainEvent event);
    
    /**
     * Publishes multiple domain events.
     * 
     * @param events the collection of domain events to publish
     */
    void publishEvents(Collection<DomainEvent> events);
    
    /**
     * Gets the underlying domain event publisher.
     * 
     * @return the domain event publisher instance
     */
    DomainEventPublisher getEventPublisher();
    
}
