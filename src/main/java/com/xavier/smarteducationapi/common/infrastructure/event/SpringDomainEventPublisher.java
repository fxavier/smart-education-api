package com.xavier.smarteducationapi.common.infrastructure.event;


import java.util.Collection;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.xavier.smarteducationapi.common.domain.event.DomainEvent;
import com.xavier.smarteducationapi.common.domain.event.DomainEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring implementation of DomainEventPublisher.
 * @version 1.0
 * @since 2025-09-15
 * @author Xavier Nhagumbe
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SpringDomainEventPublisher implements DomainEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(DomainEvent event) {
        log.debug("Publishing domain event: {} with ID: {}",
                event.getClass().getSimpleName(), event.getEventId());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAll(Collection<DomainEvent> events) {
        events.forEach(this::publish);
    }
}