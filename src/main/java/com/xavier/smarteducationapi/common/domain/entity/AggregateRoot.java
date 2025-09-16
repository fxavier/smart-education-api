package com.xavier.smarteducationapi.common.domain.entity;

import com.xavier.smarteducationapi.common.domain.event.DomainEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate root base class with generic ID type and domain events support.
 * Inherits from BaseEntity to provide common functionality for aggregate roots.
 * @version 2.0
 * @since 2025-09-15
 * @author Xavier Nhagumbe
 */
public abstract class AggregateRoot<ID> extends BaseEntity<ID> {

    private final transient List<DomainEvent> domainEvents = new ArrayList<>();
    private Long version = 0L;

    /**
     * Register a domain event to be published
     * @param event The domain event to register
     */
    protected void registerEvent(DomainEvent event) {
        if (event != null) {
            domainEvents.add(event);
        }
    }

    /**
     * Get all uncommitted domain events
     * @return Unmodifiable list of domain events
     */
    public List<DomainEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Clear all domain events after they have been published
     */
    public void markEventsAsCommitted() {
        domainEvents.clear();
    }

    /**
     * Get the aggregate version for optimistic locking
     * @return The current version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Set the aggregate version
     * @param version The new version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Increment version for optimistic locking
     */
    protected void incrementVersion() {
        this.version++;
    }
}