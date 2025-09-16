package com.xavier.smarteducationapi.tenant.domain.event;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for tenant domain events.
 * 
 * Verifies that all tenant domain events are created correctly
 * with proper data and metadata.
 * 
 * @author Xavier Nhagumbe
 */
@DisplayName("Tenant Domain Event Tests")
class TenantDomainEventTests {

    private final String tenantId = "tenant-123";
    private final String tenantName = "Test School";
    private final String subdomain = "test-school";
    private final String primaryEmail = "admin@test-school.edu";

    @Nested
    @DisplayName("TenantCreatedEvent")
    class TenantCreatedEventTests {

        @Test
        @DisplayName("Should create event with correct data")
        void shouldCreateEventWithCorrectData() {
            // When
            TenantCreatedEvent event = new TenantCreatedEvent(tenantId, tenantName, subdomain, primaryEmail);

            // Then
            assertEquals(tenantId, event.getAggregateId());
            assertEquals("Tenant", event.getAggregateType());
            assertEquals("tenant.created", event.topic());
            assertEquals(tenantName, event.getTenantName());
            assertEquals(subdomain, event.getSubdomain());
            assertEquals(primaryEmail, event.getPrimaryEmail());
            assertNotNull(event.getEventId());
            assertNotNull(event.occurredOn());
        }
    }

    @Nested
    @DisplayName("TenantActivatedEvent")
    class TenantActivatedEventTests {

        @Test
        @DisplayName("Should create event with correct data")
        void shouldCreateEventWithCorrectData() {
            // Given
            Instant activatedAt = Instant.now();

            // When
            TenantActivatedEvent event = new TenantActivatedEvent(tenantId, tenantName, activatedAt);

            // Then
            assertEquals(tenantId, event.getAggregateId());
            assertEquals("Tenant", event.getAggregateType());
            assertEquals("tenant.activated", event.topic());
            assertEquals(tenantName, event.getTenantName());
            assertEquals(activatedAt, event.getActivatedAt());
            assertNotNull(event.getEventId());
            assertNotNull(event.occurredOn());
        }
    }

    @Nested
    @DisplayName("TenantSuspendedEvent")
    class TenantSuspendedEventTests {

        @Test
        @DisplayName("Should create event with correct data")
        void shouldCreateEventWithCorrectData() {
            // Given
            String reason = "Payment overdue";
            Instant suspendedAt = Instant.now();

            // When
            TenantSuspendedEvent event = new TenantSuspendedEvent(tenantId, tenantName, reason, suspendedAt);

            // Then
            assertEquals(tenantId, event.getAggregateId());
            assertEquals("Tenant", event.getAggregateType());
            assertEquals("tenant.suspended", event.topic());
            assertEquals(tenantName, event.getTenantName());
            assertEquals(reason, event.getReason());
            assertEquals(suspendedAt, event.getSuspendedAt());
            assertNotNull(event.getEventId());
            assertNotNull(event.occurredOn());
        }
    }

    @Nested
    @DisplayName("TenantUpdatedEvent")
    class TenantUpdatedEventTests {

        @Test
        @DisplayName("Should create event with correct data")
        void shouldCreateEventWithCorrectData() {
            // Given
            String updateType = "CONTACT_INFO_UPDATED";

            // When
            TenantUpdatedEvent event = new TenantUpdatedEvent(tenantId, tenantName, updateType);

            // Then
            assertEquals(tenantId, event.getAggregateId());
            assertEquals("Tenant", event.getAggregateType());
            assertEquals("tenant.updated", event.topic());
            assertEquals(tenantName, event.getTenantName());
            assertEquals(updateType, event.getUpdateType());
            assertNotNull(event.getEventId());
            assertNotNull(event.occurredOn());
        }
    }

    @Nested
    @DisplayName("TenantFeatureEnabledEvent")
    class TenantFeatureEnabledEventTests {

        @Test
        @DisplayName("Should create event with correct data")
        void shouldCreateEventWithCorrectData() {
            // Given
            String featureCode = "ADVANCED_REPORTING";

            // When
            TenantFeatureEnabledEvent event = new TenantFeatureEnabledEvent(tenantId, tenantName, featureCode);

            // Then
            assertEquals(tenantId, event.getAggregateId());
            assertEquals("Tenant", event.getAggregateType());
            assertEquals("tenant.feature.enabled", event.topic());
            assertEquals(tenantName, event.getTenantName());
            assertEquals(featureCode, event.getFeatureCode());
            assertNotNull(event.getEventId());
            assertNotNull(event.occurredOn());
        }
    }

    @Nested
    @DisplayName("TenantFeatureDisabledEvent")
    class TenantFeatureDisabledEventTests {

        @Test
        @DisplayName("Should create event with correct data")
        void shouldCreateEventWithCorrectData() {
            // Given
            String featureCode = "ADVANCED_REPORTING";

            // When
            TenantFeatureDisabledEvent event = new TenantFeatureDisabledEvent(tenantId, tenantName, featureCode);

            // Then
            assertEquals(tenantId, event.getAggregateId());
            assertEquals("Tenant", event.getAggregateType());
            assertEquals("tenant.feature.disabled", event.topic());
            assertEquals(tenantName, event.getTenantName());
            assertEquals(featureCode, event.getFeatureCode());
            assertNotNull(event.getEventId());
            assertNotNull(event.occurredOn());
        }
    }

    @Nested
    @DisplayName("TenantLimitsUpdatedEvent")
    class TenantLimitsUpdatedEventTests {

        @Test
        @DisplayName("Should create event with correct data")
        void shouldCreateEventWithCorrectData() {
            // Given
            Integer newMaxUsers = 50;
            Integer newMaxStudents = 500;
            Integer previousMaxUsers = 10;
            Integer previousMaxStudents = 100;

            // When
            TenantLimitsUpdatedEvent event = new TenantLimitsUpdatedEvent(
                    tenantId, tenantName, newMaxUsers, newMaxStudents, previousMaxUsers, previousMaxStudents);

            // Then
            assertEquals(tenantId, event.getAggregateId());
            assertEquals("Tenant", event.getAggregateType());
            assertEquals("tenant.limits.updated", event.topic());
            assertEquals(tenantName, event.getTenantName());
            assertEquals(newMaxUsers, event.getNewMaxUsers());
            assertEquals(newMaxStudents, event.getNewMaxStudents());
            assertEquals(previousMaxUsers, event.getPreviousMaxUsers());
            assertEquals(previousMaxStudents, event.getPreviousMaxStudents());
            assertNotNull(event.getEventId());
            assertNotNull(event.occurredOn());
        }
    }
}
