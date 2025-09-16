package com.xavier.smarteducationapi.common.application.event;


import com.xavier.smarteducationapi.common.domain.entity.AggregateRoot;
import com.xavier.smarteducationapi.common.domain.event.DomainEventPublisher;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PostRemove;

/**
 * JPA listener to automatically publish domain events after transaction.
 * @version 1.0
 * @since 2025-09-15
 * @author Xavier Nhagumbe
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DomainEventListener {
    private final DomainEventPublisher eventPublisher;

    @PostPersist
    @PostUpdate
    @PostRemove
    public void publishEvents(AggregateRoot<?> aggregate) {
        var events = aggregate.getUncommittedEvents();
        if (!events.isEmpty()) {
            log.debug("Publishing {} events for aggregate: {}",
                    events.size(), aggregate.getClass().getSimpleName());
            eventPublisher.publishAll(events);
            aggregate.markEventsAsCommitted();
        }
    }
}