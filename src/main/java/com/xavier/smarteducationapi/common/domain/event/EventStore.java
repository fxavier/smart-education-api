package com.xavier.smarteducationapi.common.domain.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Interface for event store operations.
 * @version 1.0
 * @since 2025-09-15
 * @author Xavier Nhagumbe
 */
public interface EventStore {
    /**
     * Store an event
     */
    void store(DomainEvent event);

    /**
     * Retrieve events for an aggregate
     */
    List<DomainEvent> getEventsForAggregate(String aggregateId, String aggregateType);

    /**
     * Retrieve events by time range
     */
    List<DomainEvent> getEventsByTimeRange(Instant start, Instant end);

    /**
     * Retrieve events by topic
     */
    List<DomainEvent> getEventsByTopic(String topic);

    /**
     * Get event by ID
     */
    DomainEvent getEventById(UUID eventId);
}