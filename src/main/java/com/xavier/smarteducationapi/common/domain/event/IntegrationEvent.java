package com.xavier.smarteducationapi.common.domain.event;
/**
 * Marker interface for integration events between bounded contexts.
 * @version 1.0
 * @since 2025-09-15
 * @author Xavier Nhagumbe
 */
public interface IntegrationEvent extends DomainEvent {
    /**
     * Source bounded context that published this event
     */
    String getSourceContext();

    /**
     * Target bounded contexts that should receive this event
     */
    default String[] getTargetContexts() {
        return new String[]{"*"}; // All contexts by default
    }
}
