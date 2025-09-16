package com.xavier.smarteducationapi.common.domain.event;

import java.time.Instant;
import java.util.UUID;
import java.util.Objects;

/**
 * Abstract base class for domain events.
 * @version 1.0
 * @since 2025-09-15
 * @author Xavier Nhagumbe
 */
public abstract class AbstractDomainEvent implements DomainEvent {
    private final UUID eventId;
    private final Instant occurredOn;
    private final String aggregateId;
    private final String aggregateType;

    protected AbstractDomainEvent(String aggregateId, String aggregateType) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = Instant.now();
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Instant occurredOn() {
        return occurredOn;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return aggregateType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDomainEvent that = (AbstractDomainEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
}
