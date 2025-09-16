package com.xavier.smarteducationapi.common.infrastructure.exception;

import com.xavier.smarteducationapi.common.domain.event.DomainEvent;

/**
 * Exception thrown when domain event publishing fails.
 * 
 * This can occur due to messaging system failures, serialization issues,
 * or other technical problems during event publishing.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class EventPublishingException extends InfrastructureException {
    
    private static final String ERROR_CODE = "EVENT_PUBLISHING_FAILED";
    
    private final String eventType;
    private final String eventId;
    
    public EventPublishingException(String message) {
        super(ERROR_CODE, message);
        this.eventType = null;
        this.eventId = null;
    }
    
    public EventPublishingException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
        this.eventType = null;
        this.eventId = null;
    }
    
    public EventPublishingException(DomainEvent event, String message, Throwable cause) {
        super(ERROR_CODE, String.format("Failed to publish event '%s' with id '%s': %s", 
                event.getClass().getSimpleName(), event.getEventId(), message), cause);
        this.eventType = event.getClass().getSimpleName();
        this.eventId = event.getEventId().toString();
    }
    
    public EventPublishingException(String eventType, String eventId, String message, Throwable cause) {
        super(ERROR_CODE, String.format("Failed to publish event '%s' with id '%s': %s", 
                eventType, eventId, message), cause);
        this.eventType = eventType;
        this.eventId = eventId;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    public String getEventId() {
        return eventId;
    }
    
    /**
     * Convenience method for serialization failures.
     */
    public static EventPublishingException serializationFailed(DomainEvent event, Throwable cause) {
        return new EventPublishingException(event, "Event serialization failed", cause);
    }
    
    /**
     * Convenience method for messaging system failures.
     */
    public static EventPublishingException messagingSystemFailed(DomainEvent event, Throwable cause) {
        return new EventPublishingException(event, "Messaging system failure", cause);
    }
}
