package com.xavier.smarteducationapi.common.domain.event;

import java.util.Collection;

/**
 * Interface for publishing domain events.
 * @version 1.0
 * @since 2025-09-15
 * @author Xavier Nhagumbe
 */
public interface DomainEventPublisher {
    /**
     * Publish a single domain event
     */
    void publish(DomainEvent event);

    /**
     * Publish multiple domain events
     */
    void publishAll(Collection<DomainEvent> events);
}