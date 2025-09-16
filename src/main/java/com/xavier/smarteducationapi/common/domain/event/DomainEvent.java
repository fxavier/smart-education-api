package com.xavier.smarteducationapi.common.domain.event;
import java.time.Instant;
import java.util.UUID;

/**
 * Enhanced interface representing a domain event.
 * @version 2.0
 * @since 2025-09-15
 * @author Xavier Nhagumbe
 */
public interface DomainEvent {
    /**
     * Unique identifier for this event
     */
    UUID getEventId();

    /**
     * When the event occurred
     */
    Instant occurredOn();

    /**
     * Topic/Category of the event for routing
     */
    String topic();

    /**
     * Aggregate ID that generated this event
     */
    String getAggregateId();

    /**
     * Type of aggregate that generated this event
     */
    String getAggregateType();

    /**
     * Version of the event schema
     */
    default int getEventVersion() {
        return 1;
    }
}