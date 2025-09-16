package com.xavier.smarteducationapi.common.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import com.xavier.smarteducationapi.common.domain.event.AbstractDomainEvent;
import com.xavier.smarteducationapi.common.domain.event.DomainEvent;
import com.xavier.smarteducationapi.common.domain.event.DomainEventPublisher;
import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;

/**
 * Tests for domain event flow and integration.
 * 
 * These tests verify that domain events are properly published,
 * handled, and flow correctly through the system.
 * 
 * @author Xavier Nhagumbe
 */
@SpringBootTest
@ActiveProfiles("test")
class DomainEventFlowTests {

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private TestEventHandler testEventHandler;

    /**
     * Test domain event for testing purposes.
     */
    public static class TestDomainEvent extends AbstractDomainEvent {
        private final String message;
        private final TenantId tenantId;

        public TestDomainEvent(String message, TenantId tenantId) {
            super(tenantId.toString(), "TestAggregate");
            this.message = message;
            this.tenantId = tenantId;
        }

        @Override
        public String topic() {
            return "test.domain.event";
        }

        public String getMessage() {
            return message;
        }

        public TenantId getTenantId() {
            return tenantId;
        }
    }

    /**
     * Integration event for cross-module communication.
     */
    public static class TestIntegrationEvent extends TestDomainEvent {
        public TestIntegrationEvent(String message, TenantId tenantId) {
            super(message, tenantId);
        }
        
        @Override
        public String topic() {
            return "test.integration.event";
        }
    }

    @Test
    void shouldPublishSingleDomainEvent() throws InterruptedException {
        // Given
        TenantId tenantId = new TenantId(UUID.randomUUID());
        TestDomainEvent event = new TestDomainEvent("Test message", tenantId);
        testEventHandler.reset();

        // When
        domainEventPublisher.publish(event);

        // Then
        assertTrue(testEventHandler.awaitEvent(5, TimeUnit.SECONDS));
        verify(applicationEventPublisher, times(1)).publishEvent(any(DomainEvent.class));
        
        List<DomainEvent> handledEvents = testEventHandler.getHandledEvents();
        assertEquals(1, handledEvents.size());
        
        TestDomainEvent handledEvent = (TestDomainEvent) handledEvents.get(0);
        assertEquals("Test message", handledEvent.getMessage());
        assertEquals(tenantId, handledEvent.getTenantId());
    }

    @Test
    void shouldPublishMultipleDomainEvents() throws InterruptedException {
        // Given
        TenantId tenantId = new TenantId(UUID.randomUUID());
        List<DomainEvent> events = List.of(
            new TestDomainEvent("Message 1", tenantId),
            new TestDomainEvent("Message 2", tenantId),
            new TestIntegrationEvent("Integration message", tenantId)
        );
        testEventHandler.reset();

        // When
        domainEventPublisher.publishAll(events);

        // Then
        assertTrue(testEventHandler.awaitEvents(3, 5, TimeUnit.SECONDS));
        verify(applicationEventPublisher, times(3)).publishEvent(any(DomainEvent.class));
        
        List<DomainEvent> handledEvents = testEventHandler.getHandledEvents();
        assertEquals(3, handledEvents.size());
    }

    @Test
    void shouldHandleIntegrationEvents() throws InterruptedException {
        // Given
        TenantId tenantId = new TenantId(UUID.randomUUID());
        TestIntegrationEvent event = new TestIntegrationEvent("Integration test", tenantId);
        testEventHandler.reset();

        // When
        domainEventPublisher.publish(event);

        // Then
        assertTrue(testEventHandler.awaitEvent(5, TimeUnit.SECONDS));
        
        List<DomainEvent> handledEvents = testEventHandler.getHandledEvents();
        assertEquals(1, handledEvents.size());
        
        TestIntegrationEvent handledEvent = (TestIntegrationEvent) handledEvents.get(0);
        assertEquals("Integration test", handledEvent.getMessage());
        assertEquals(tenantId, handledEvent.getTenantId());
    }

    @Test
    void shouldPreserveEventMetadata() throws InterruptedException {
        // Given
        TenantId tenantId = new TenantId(UUID.randomUUID());
        TestDomainEvent event = new TestDomainEvent("Metadata test", tenantId);
        testEventHandler.reset();

        // When
        domainEventPublisher.publish(event);

        // Then
        assertTrue(testEventHandler.awaitEvent(5, TimeUnit.SECONDS));
        
        List<DomainEvent> handledEvents = testEventHandler.getHandledEvents();
        TestDomainEvent handledEvent = (TestDomainEvent) handledEvents.get(0);
        
        assertNotNull(handledEvent.getEventId());
        assertNotNull(handledEvent.occurredOn());
        assertEquals(event.getEventId(), handledEvent.getEventId());
        assertEquals(event.occurredOn(), handledEvent.occurredOn());
    }

    /**
     * Test event handler component for capturing handled events.
     */
    @Component
    public static class TestEventHandler {
        private CountDownLatch latch = new CountDownLatch(1);
        private java.util.List<DomainEvent> handledEvents = new java.util.ArrayList<>();

        @EventListener
        public void handle(TestDomainEvent event) {
            synchronized (this) {
                handledEvents.add(event);
                latch.countDown();
            }
        }

        public void reset() {
            synchronized (this) {
                handledEvents.clear();
                latch = new CountDownLatch(1);
            }
        }

        public boolean awaitEvent(long timeout, TimeUnit unit) throws InterruptedException {
            return latch.await(timeout, unit);
        }

        public boolean awaitEvents(int count, long timeout, TimeUnit unit) throws InterruptedException {
            latch = new CountDownLatch(count);
            return latch.await(timeout, unit);
        }

        public List<DomainEvent> getHandledEvents() {
            synchronized (this) {
                return new java.util.ArrayList<>(handledEvents);
            }
        }
    }
}
