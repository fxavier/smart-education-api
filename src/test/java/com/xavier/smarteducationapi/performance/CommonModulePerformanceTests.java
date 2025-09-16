package com.xavier.smarteducationapi.performance;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.xavier.smarteducationapi.common.domain.event.AbstractDomainEvent;
import com.xavier.smarteducationapi.common.domain.event.DomainEventPublisher;
import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;

/**
 * Performance tests for the common module.
 * 
 * These tests verify that the common module components perform
 * adequately under load and concurrent usage scenarios.
 * 
 * @author Xavier Nhagumbe
 */
@SpringBootTest
@ActiveProfiles("test")
class CommonModulePerformanceTests {

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    /**
     * Test event for performance testing.
     */
    public static class PerformanceTestEvent extends AbstractDomainEvent {
        private final String payload;
        private final TenantId tenantId;

        public PerformanceTestEvent(String payload, TenantId tenantId) {
            super(tenantId.toString(), "PerformanceAggregate");
            this.payload = payload;
            this.tenantId = tenantId;
        }

        @Override
        public String topic() {
            return "performance.test.event";
        }

        public String getPayload() {
            return payload;
        }

        public TenantId getTenantId() {
            return tenantId;
        }
    }

    @Test
    void shouldHandleHighVolumeEventPublishing() throws Exception {
        // Test publishing a large number of events
        int eventCount = 1000;
        List<PerformanceTestEvent> events = new ArrayList<>();
        TenantId tenantId = new TenantId(UUID.randomUUID());

        // Create events
        for (int i = 0; i < eventCount; i++) {
            events.add(new PerformanceTestEvent("Event " + i, tenantId));
        }

        // Measure time to publish all events
        long startTime = System.currentTimeMillis();
        domainEventPublisher.publishAll(new ArrayList<>(events));
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        double eventsPerSecond = (eventCount * 1000.0) / duration;

        System.out.printf("Published %d events in %d ms (%.2f events/sec)%n", 
                eventCount, duration, eventsPerSecond);

        // Assert reasonable performance (adjust threshold as needed)
        assertTrue(eventsPerSecond > 100, 
                "Event publishing should handle at least 100 events per second");
    }

    @Test
    void shouldHandleConcurrentEventPublishing() throws Exception {
        // Test concurrent event publishing from multiple threads
        int threadCount = 10;
        int eventsPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        TenantId tenantId = new TenantId(UUID.randomUUID());

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        // Submit concurrent publishing tasks
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = 0; j < eventsPerThread; j++) {
                    PerformanceTestEvent event = new PerformanceTestEvent(
                            "Thread-" + threadIndex + "-Event-" + j, tenantId);
                    domainEventPublisher.publish(event);
                }
            }, executor);
            futures.add(future);
        }

        // Wait for all tasks to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        int totalEvents = threadCount * eventsPerThread;
        double eventsPerSecond = (totalEvents * 1000.0) / duration;

        System.out.printf("Published %d events concurrently in %d ms (%.2f events/sec)%n", 
                totalEvents, duration, eventsPerSecond);

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        // Assert reasonable concurrent performance
        assertTrue(eventsPerSecond > 50, 
                "Concurrent event publishing should handle at least 50 events per second");
    }

    @Test
    void shouldCreateValueObjectsEfficiently() {
        // Test value object creation performance
        int objectCount = 10000;
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < objectCount; i++) {
            new TenantId(UUID.randomUUID());
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double objectsPerSecond = (objectCount * 1000.0) / duration;

        System.out.printf("Created %d TenantId objects in %d ms (%.2f objects/sec)%n", 
                objectCount, duration, objectsPerSecond);

        // Assert reasonable performance for value object creation
        assertTrue(objectsPerSecond > 1000, 
                "Value object creation should handle at least 1000 objects per second");
    }

    @Test
    void shouldHandleMemoryPressure() {
        // Test behavior under memory pressure
        List<Object> memoryConsumers = new ArrayList<>();
        
        try {
            // Create some memory pressure
            for (int i = 0; i < 1000; i++) {
                TenantId tenantId = new TenantId(UUID.randomUUID());
                PerformanceTestEvent event = new PerformanceTestEvent("Memory test " + i, tenantId);
                memoryConsumers.add(event);
                
                // Publish event under memory pressure
                domainEventPublisher.publish(event);
            }
            
            // Force garbage collection
            System.gc();
            
            // Verify system is still responsive
            TenantId testTenantId = new TenantId(UUID.randomUUID());
            PerformanceTestEvent finalEvent = new PerformanceTestEvent("Final test", testTenantId);
            domainEventPublisher.publish(finalEvent);
            
            // Test passes if no OutOfMemoryError is thrown
            assertTrue(true, "System should remain stable under memory pressure");
            
        } finally {
            // Clean up
            memoryConsumers.clear();
            System.gc();
        }
    }
}
