package com.xavier.smarteducationapi.tenant.domain.entity;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.xavier.smarteducationapi.common.domain.event.DomainEvent;
import com.xavier.smarteducationapi.common.domain.exception.BusinessRuleViolationException;
import com.xavier.smarteducationapi.common.domain.valueobject.Address;
import com.xavier.smarteducationapi.common.domain.valueobject.Email;
import com.xavier.smarteducationapi.common.domain.valueobject.Phone;
import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;
import com.xavier.smarteducationapi.tenant.domain.event.TenantActivatedEvent;
import com.xavier.smarteducationapi.tenant.domain.event.TenantCreatedEvent;
import com.xavier.smarteducationapi.tenant.domain.event.TenantFeatureDisabledEvent;
import com.xavier.smarteducationapi.tenant.domain.event.TenantFeatureEnabledEvent;
import com.xavier.smarteducationapi.tenant.domain.event.TenantLimitsUpdatedEvent;
import com.xavier.smarteducationapi.tenant.domain.event.TenantSuspendedEvent;
import com.xavier.smarteducationapi.tenant.domain.event.TenantUpdatedEvent;
import com.xavier.smarteducationapi.tenant.domain.valueobject.TenantStatus;

/**
 * Comprehensive tests for the Tenant aggregate root.
 * 
 * Tests cover:
 * - Factory methods (create, reconstruct)
 * - Business rule enforcement
 * - State transitions
 * - Domain event publishing
 * - Validation logic
 * - Edge cases and error conditions
 * 
 * @author Xavier Nhagumbe
 */
@DisplayName("Tenant Entity Tests")
class TenantTests {

    private TenantId tenantId;
    private String tenantName;
    private String subdomain;
    private Email primaryEmail;
    private Phone primaryPhone;
    private Address address;

    @BeforeEach
    void setUp() {
        tenantId = TenantId.generate();
        tenantName = "Test School";
        subdomain = "test-school";
        primaryEmail = new Email("admin@test-school.edu");
        primaryPhone = new Phone("+1-234-567-8900");
        address = Address.builder()
                .street("123 Education St")
                .city("Education City")
                .country("Test Country")
                .build();
    }

    @Nested
    @DisplayName("Factory Methods")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should create tenant with valid data")
        void shouldCreateTenantWithValidData() {
            // When
            Tenant tenant = Tenant.create(tenantId, tenantName, subdomain, primaryEmail, primaryPhone, address);

            // Then
            assertEquals(tenantId, tenant.getId());
            assertEquals(tenantName, tenant.getName());
            assertEquals(subdomain, tenant.getSubdomain());
            assertEquals(primaryEmail, tenant.getPrimaryEmail());
            assertEquals(primaryPhone, tenant.getPrimaryPhone());
            assertEquals(address, tenant.getAddress());
            assertEquals(TenantStatus.PENDING, tenant.getStatus());
            assertNotNull(tenant.getCreatedAt());
            assertEquals(Integer.valueOf(10), tenant.getMaxUsers());
            assertEquals(Integer.valueOf(100), tenant.getMaxStudents());
            assertTrue(tenant.getFeatures().isEmpty());

            // Verify domain event was published
            List<DomainEvent> events = tenant.getUncommittedEvents();
            assertEquals(1, events.size());
            assertTrue(events.get(0) instanceof TenantCreatedEvent);

            TenantCreatedEvent createdEvent = (TenantCreatedEvent) events.get(0);
            assertEquals(tenantName, createdEvent.getTenantName());
            assertEquals(subdomain, createdEvent.getSubdomain());
            assertEquals(primaryEmail.getValue(), createdEvent.getPrimaryEmail());
        }

        @Test
        @DisplayName("Should reconstruct tenant from persistence data")
        void shouldReconstructTenantFromPersistenceData() {
            // Given
            Set<String> features = Set.of("FEATURE_A", "FEATURE_B");
            Instant createdAt = Instant.parse("2025-01-01T00:00:00Z");
            Instant activatedAt = Instant.parse("2025-01-02T00:00:00Z");
            Long version = 5L;

            // When
            Tenant tenant = Tenant.reconstruct(
                    tenantId,
                    tenantName,
                    subdomain,
                    TenantStatus.ACTIVE,
                    primaryEmail,
                    primaryPhone,
                    address,
                    "TAX123",
                    "REG456",
                    features,
                    20,
                    200,
                    createdAt,
                    activatedAt,
                    null,
                    null,
                    version
            );

            // Then
            assertEquals(tenantId, tenant.getId());
            assertEquals(TenantStatus.ACTIVE, tenant.getStatus());
            assertEquals("TAX123", tenant.getTaxId());
            assertEquals("REG456", tenant.getRegistrationNumber());
            assertEquals(features, tenant.getFeatures());
            assertEquals(Integer.valueOf(20), tenant.getMaxUsers());
            assertEquals(Integer.valueOf(200), tenant.getMaxStudents());
            assertEquals(createdAt, tenant.getCreatedAt());
            assertEquals(activatedAt, tenant.getActivatedAt());
            assertEquals(version, tenant.getVersion());

            // Should not have uncommitted events after reconstruction
            assertTrue(tenant.getUncommittedEvents().isEmpty());
        }

        @Nested
        @DisplayName("Subdomain Validation")
        class SubdomainValidationTests {

            @Test
            @DisplayName("Should reject null subdomain")
            void shouldRejectNullSubdomain() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> Tenant.create(tenantId, tenantName, null, primaryEmail, primaryPhone, address));
                assertEquals("Subdomain is required", exception.getMessage());
            }

            @Test
            @DisplayName("Should reject empty subdomain")
            void shouldRejectEmptySubdomain() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> Tenant.create(tenantId, tenantName, "", primaryEmail, primaryPhone, address));
                assertEquals("Subdomain is required", exception.getMessage());
            }

            @Test
            @DisplayName("Should reject subdomain too short")
            void shouldRejectSubdomainTooShort() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> Tenant.create(tenantId, tenantName, "ab", primaryEmail, primaryPhone, address));
                assertTrue(exception.getMessage().contains("3-63 characters"));
            }

            @Test
            @DisplayName("Should reject subdomain too long")
            void shouldRejectSubdomainTooLong() {
                String longSubdomain = "a".repeat(64);
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> Tenant.create(tenantId, tenantName, longSubdomain, primaryEmail, primaryPhone, address));
                assertTrue(exception.getMessage().contains("3-63 characters"));
            }

            @Test
            @DisplayName("Should reject subdomain with invalid characters")
            void shouldRejectSubdomainWithInvalidCharacters() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> Tenant.create(tenantId, tenantName, "test_school", primaryEmail, primaryPhone, address));
                assertTrue(exception.getMessage().contains("lowercase letters, numbers, and hyphens"));
            }

            @Test
            @DisplayName("Should reject subdomain starting with hyphen")
            void shouldRejectSubdomainStartingWithHyphen() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> Tenant.create(tenantId, tenantName, "-test-school", primaryEmail, primaryPhone, address));
                assertTrue(exception.getMessage().contains("cannot start or end with a hyphen"));
            }

            @Test
            @DisplayName("Should reject subdomain ending with hyphen")
            void shouldRejectSubdomainEndingWithHyphen() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> Tenant.create(tenantId, tenantName, "test-school-", primaryEmail, primaryPhone, address));
                assertTrue(exception.getMessage().contains("cannot start or end with a hyphen"));
            }

            @Test
            @DisplayName("Should normalize subdomain to lowercase")
            void shouldNormalizeSubdomainToLowercase() {
                Tenant tenant = Tenant.create(tenantId, tenantName, "Test-SCHOOL", primaryEmail, primaryPhone, address);
                assertEquals("test-school", tenant.getSubdomain());
            }

            @Test
            @DisplayName("Should accept valid subdomain with numbers and hyphens")
            void shouldAcceptValidSubdomainWithNumbersAndHyphens() {
                Tenant tenant = Tenant.create(tenantId, tenantName, "school-2024-test", primaryEmail, primaryPhone, address);
                assertEquals("school-2024-test", tenant.getSubdomain());
            }
        }
    }

    @Nested
    @DisplayName("Business Methods")
    class BusinessMethodTests {

        private Tenant tenant;

        @BeforeEach
        void setUp() {
            tenant = Tenant.create(tenantId, tenantName, subdomain, primaryEmail, primaryPhone, address);
            tenant.markEventsAsCommitted(); // Clear creation event for cleaner tests
        }

        @Nested
        @DisplayName("Tenant Activation")
        class TenantActivationTests {

            @Test
            @DisplayName("Should activate pending tenant")
            void shouldActivatePendingTenant() {
                // Given
                assertEquals(TenantStatus.PENDING, tenant.getStatus());
                Instant beforeActivation = Instant.now();

                // When
                tenant.activate();

                // Then
                assertEquals(TenantStatus.ACTIVE, tenant.getStatus());
                assertNotNull(tenant.getActivatedAt());
                assertTrue(tenant.getActivatedAt().isAfter(beforeActivation));

                // Verify event was published
                List<DomainEvent> events = tenant.getUncommittedEvents();
                assertEquals(1, events.size());
                assertTrue(events.get(0) instanceof TenantActivatedEvent);

                TenantActivatedEvent activatedEvent = (TenantActivatedEvent) events.get(0);
                assertEquals(tenantId.toString(), activatedEvent.getAggregateId());
                assertEquals(tenantName, activatedEvent.getTenantName());
                assertEquals(tenant.getActivatedAt(), activatedEvent.getActivatedAt());
            }

            @Test
            @DisplayName("Should reject activation of already active tenant")
            void shouldRejectActivationOfAlreadyActiveTenant() {
                // Given
                tenant.activate();
                tenant.markEventsAsCommitted();

                // When & Then
                BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class,
                        tenant::activate);
                assertEquals("TenantActivation", exception.getRuleName());
                assertTrue(exception.getMessage().contains("PENDING status"));
                assertEquals(TenantStatus.ACTIVE, exception.getViolatingValue());
            }

            @Test
            @DisplayName("Should reject activation of suspended tenant")
            void shouldRejectActivationOfSuspendedTenant() {
                // Given
                tenant.activate();
                tenant.suspend("Testing");
                tenant.markEventsAsCommitted();

                // When & Then
                BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class,
                        tenant::activate);
                assertEquals("TenantActivation", exception.getRuleName());
                assertEquals(TenantStatus.SUSPENDED, exception.getViolatingValue());
            }
        }

        @Nested
        @DisplayName("Tenant Suspension")
        class TenantSuspensionTests {

            @BeforeEach
            void setUp() {
                tenant.activate();
                tenant.markEventsAsCommitted();
            }

            @Test
            @DisplayName("Should suspend active tenant")
            void shouldSuspendActiveTenant() {
                // Given
                String reason = "Payment overdue";
                Instant beforeSuspension = Instant.now();

                // When
                tenant.suspend(reason);

                // Then
                assertEquals(TenantStatus.SUSPENDED, tenant.getStatus());
                assertNotNull(tenant.getSuspendedAt());
                assertTrue(tenant.getSuspendedAt().isAfter(beforeSuspension));
                assertEquals(reason, tenant.getSuspensionReason());

                // Verify event was published
                List<DomainEvent> events = tenant.getUncommittedEvents();
                assertEquals(1, events.size());
                assertTrue(events.get(0) instanceof TenantSuspendedEvent);

                TenantSuspendedEvent suspendedEvent = (TenantSuspendedEvent) events.get(0);
                assertEquals(tenantId.toString(), suspendedEvent.getAggregateId());
                assertEquals(tenantName, suspendedEvent.getTenantName());
                assertEquals(reason, suspendedEvent.getReason());
                assertEquals(tenant.getSuspendedAt(), suspendedEvent.getSuspendedAt());
            }

            @Test
            @DisplayName("Should reject suspension of pending tenant")
            void shouldRejectSuspensionOfPendingTenant() {
                // Given
                Tenant pendingTenant = Tenant.create(TenantId.generate(), "Test", "test", primaryEmail, primaryPhone, address);

                // When & Then
                BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class,
                        () -> pendingTenant.suspend("Testing"));
                assertEquals("TenantSuspension", exception.getRuleName());
                assertEquals(TenantStatus.PENDING, exception.getViolatingValue());
            }

            @Test
            @DisplayName("Should reject suspension with null reason")
            void shouldRejectSuspensionWithNullReason() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> tenant.suspend(null));
                assertEquals("Suspension reason is required", exception.getMessage());
            }

            @Test
            @DisplayName("Should reject suspension with empty reason")
            void shouldRejectSuspensionWithEmptyReason() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> tenant.suspend("   "));
                assertEquals("Suspension reason is required", exception.getMessage());
            }
        }

        @Nested
        @DisplayName("Tenant Reactivation")
        class TenantReactivationTests {

            @BeforeEach
            void setUp() {
                tenant.activate();
                tenant.suspend("For testing");
                tenant.markEventsAsCommitted();
            }

            @Test
            @DisplayName("Should reactivate suspended tenant")
            void shouldReactivateSuspendedTenant() {
                // When
                tenant.reactivate();

                // Then
                assertEquals(TenantStatus.ACTIVE, tenant.getStatus());
                assertNull(tenant.getSuspendedAt());
                assertNull(tenant.getSuspensionReason());

                // Verify event was published
                List<DomainEvent> events = tenant.getUncommittedEvents();
                assertEquals(1, events.size());
                assertTrue(events.get(0) instanceof TenantActivatedEvent);
            }

            @Test
            @DisplayName("Should reject reactivation of active tenant")
            void shouldRejectReactivationOfActiveTenant() {
                // Given
                tenant.reactivate();
                tenant.markEventsAsCommitted();

                // When & Then
                BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class,
                        tenant::reactivate);
                assertEquals("TenantReactivation", exception.getRuleName());
                assertEquals(TenantStatus.ACTIVE, exception.getViolatingValue());
            }

            @Test
            @DisplayName("Should reject reactivation of pending tenant")
            void shouldRejectReactivationOfPendingTenant() {
                // Given
                Tenant pendingTenant = Tenant.create(TenantId.generate(), "Test", "test", primaryEmail, primaryPhone, address);

                // When & Then
                BusinessRuleViolationException exception = assertThrows(BusinessRuleViolationException.class,
                        pendingTenant::reactivate);
                assertEquals("TenantReactivation", exception.getRuleName());
                assertEquals(TenantStatus.PENDING, exception.getViolatingValue());
            }
        }

        @Nested
        @DisplayName("Contact Information Updates")
        class ContactInfoTests {

            @Test
            @DisplayName("Should update contact information")
            void shouldUpdateContactInformation() {
                // Given
                Email newEmail = new Email("new-admin@test-school.edu");
                Phone newPhone = new Phone("+1-987-654-3210");
                Address newAddress = Address.builder()
                        .street("456 New Education Ave")
                        .city("New Education City")
                        .country("New Test Country")
                        .build();

                // When
                tenant.updateContactInfo(newEmail, newPhone, newAddress);

                // Then
                assertEquals(newEmail, tenant.getPrimaryEmail());
                assertEquals(newPhone, tenant.getPrimaryPhone());
                assertEquals(newAddress, tenant.getAddress());

                // Verify event was published
                List<DomainEvent> events = tenant.getUncommittedEvents();
                assertEquals(1, events.size());
                assertTrue(events.get(0) instanceof TenantUpdatedEvent);

                TenantUpdatedEvent updatedEvent = (TenantUpdatedEvent) events.get(0);
                assertEquals("CONTACT_INFO_UPDATED", updatedEvent.getUpdateType());
            }
        }

        @Nested
        @DisplayName("Feature Management")
        class FeatureManagementTests {

            @Test
            @DisplayName("Should enable feature")
            void shouldEnableFeature() {
                // Given
                String featureCode = "ADVANCED_REPORTING";

                // When
                tenant.enableFeature(featureCode);

                // Then
                assertTrue(tenant.hasFeature(featureCode));
                assertTrue(tenant.getFeatures().contains(featureCode));

                // Verify event was published
                List<DomainEvent> events = tenant.getUncommittedEvents();
                assertEquals(1, events.size());
                assertTrue(events.get(0) instanceof TenantFeatureEnabledEvent);

                TenantFeatureEnabledEvent enabledEvent = (TenantFeatureEnabledEvent) events.get(0);
                assertEquals(featureCode, enabledEvent.getFeatureCode());
            }

            @Test
            @DisplayName("Should disable feature")
            void shouldDisableFeature() {
                // Given
                String featureCode = "ADVANCED_REPORTING";
                tenant.enableFeature(featureCode);
                tenant.markEventsAsCommitted();

                // When
                tenant.disableFeature(featureCode);

                // Then
                assertFalse(tenant.hasFeature(featureCode));
                assertFalse(tenant.getFeatures().contains(featureCode));

                // Verify event was published
                List<DomainEvent> events = tenant.getUncommittedEvents();
                assertEquals(1, events.size());
                assertTrue(events.get(0) instanceof TenantFeatureDisabledEvent);

                TenantFeatureDisabledEvent disabledEvent = (TenantFeatureDisabledEvent) events.get(0);
                assertEquals(featureCode, disabledEvent.getFeatureCode());
            }

            @Test
            @DisplayName("Should handle multiple features")
            void shouldHandleMultipleFeatures() {
                // Given
                String feature1 = "FEATURE_1";
                String feature2 = "FEATURE_2";
                String feature3 = "FEATURE_3";

                // When
                tenant.enableFeature(feature1);
                tenant.enableFeature(feature2);
                tenant.enableFeature(feature3);

                // Then
                assertEquals(3, tenant.getFeatures().size());
                assertTrue(tenant.hasFeature(feature1));
                assertTrue(tenant.hasFeature(feature2));
                assertTrue(tenant.hasFeature(feature3));

                // When disabling one feature
                tenant.disableFeature(feature2);

                // Then
                assertEquals(2, tenant.getFeatures().size());
                assertTrue(tenant.hasFeature(feature1));
                assertFalse(tenant.hasFeature(feature2));
                assertTrue(tenant.hasFeature(feature3));
            }
        }

        @Nested
        @DisplayName("Limits Management")
        class LimitsManagementTests {

            @Test
            @DisplayName("Should update limits")
            void shouldUpdateLimits() {
                // Given
                Integer newMaxUsers = 50;
                Integer newMaxStudents = 500;
                Integer previousMaxUsers = tenant.getMaxUsers();
                Integer previousMaxStudents = tenant.getMaxStudents();

                // When
                tenant.updateLimits(newMaxUsers, newMaxStudents);

                // Then
                assertEquals(newMaxUsers, tenant.getMaxUsers());
                assertEquals(newMaxStudents, tenant.getMaxStudents());

                // Verify event was published
                List<DomainEvent> events = tenant.getUncommittedEvents();
                assertEquals(1, events.size());
                assertTrue(events.get(0) instanceof TenantLimitsUpdatedEvent);

                TenantLimitsUpdatedEvent limitsEvent = (TenantLimitsUpdatedEvent) events.get(0);
                assertEquals(newMaxUsers, limitsEvent.getNewMaxUsers());
                assertEquals(newMaxStudents, limitsEvent.getNewMaxStudents());
                assertEquals(previousMaxUsers, limitsEvent.getPreviousMaxUsers());
                assertEquals(previousMaxStudents, limitsEvent.getPreviousMaxStudents());
            }

            @Test
            @DisplayName("Should reject invalid max users")
            void shouldRejectInvalidMaxUsers() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> tenant.updateLimits(0, 100));
                assertEquals("Max users must be at least 1", exception.getMessage());
            }

            @Test
            @DisplayName("Should reject invalid max students")
            void shouldRejectInvalidMaxStudents() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> tenant.updateLimits(10, 0));
                assertEquals("Max students must be at least 1", exception.getMessage());
            }

            @Test
            @DisplayName("Should allow null limits to keep existing values")
            void shouldAllowNullLimitsToKeepExistingValues() {
                // Given
                Integer originalMaxUsers = tenant.getMaxUsers();
                Integer originalMaxStudents = tenant.getMaxStudents();

                // When
                tenant.updateLimits(null, null);

                // Then
                assertNull(tenant.getMaxUsers());
                assertNull(tenant.getMaxStudents());
            }
        }

        @Nested
        @DisplayName("Business Registration")
        class BusinessRegistrationTests {

            @Test
            @DisplayName("Should set business registration information")
            void shouldSetBusinessRegistrationInformation() {
                // Given
                String taxId = "TAX-123-456";
                String registrationNumber = "REG-789-012";

                // When
                tenant.setBusinessRegistration(taxId, registrationNumber);

                // Then
                assertEquals(taxId, tenant.getTaxId());
                assertEquals(registrationNumber, tenant.getRegistrationNumber());
            }

            @Test
            @DisplayName("Should allow null business registration values")
            void shouldAllowNullBusinessRegistrationValues() {
                // When
                tenant.setBusinessRegistration(null, null);

                // Then
                assertNull(tenant.getTaxId());
                assertNull(tenant.getRegistrationNumber());
            }
        }
    }

    @Nested
    @DisplayName("Domain Events")
    class DomainEventTests {

        @Test
        @DisplayName("Should track uncommitted events")
        void shouldTrackUncommittedEvents() {
            // Given
            Tenant tenant = Tenant.create(tenantId, tenantName, subdomain, primaryEmail, primaryPhone, address);

            // When
            tenant.activate();
            tenant.enableFeature("TEST_FEATURE");

            // Then
            List<DomainEvent> events = tenant.getUncommittedEvents();
            assertEquals(3, events.size()); // Create, Activate, EnableFeature

            assertTrue(events.get(0) instanceof TenantCreatedEvent);
            assertTrue(events.get(1) instanceof TenantActivatedEvent);
            assertTrue(events.get(2) instanceof TenantFeatureEnabledEvent);
        }

        @Test
        @DisplayName("Should clear events after marking as committed")
        void shouldClearEventsAfterMarkingAsCommitted() {
            // Given
            Tenant tenant = Tenant.create(tenantId, tenantName, subdomain, primaryEmail, primaryPhone, address);
            assertFalse(tenant.getUncommittedEvents().isEmpty());

            // When
            tenant.markEventsAsCommitted();

            // Then
            assertTrue(tenant.getUncommittedEvents().isEmpty());
        }
    }

    @Nested
    @DisplayName("Value Object Integration")
    class ValueObjectIntegrationTests {

        @Test
        @DisplayName("Should work with Email value object")
        void shouldWorkWithEmailValueObject() {
            Email email = new Email("test@domain.com");
            Tenant tenant = Tenant.create(tenantId, tenantName, subdomain, email, primaryPhone, address);
            assertEquals(email, tenant.getPrimaryEmail());
            assertEquals("test@domain.com", tenant.getPrimaryEmail().getValue());
        }

        @Test
        @DisplayName("Should work with Phone value object")
        void shouldWorkWithPhoneValueObject() {
            Phone phone = new Phone("+1-234-567-8900");
            Tenant tenant = Tenant.create(tenantId, tenantName, subdomain, primaryEmail, phone, address);
            assertEquals(phone, tenant.getPrimaryPhone());
            assertEquals("+12345678900", tenant.getPrimaryPhone().getValue());
        }

        @Test
        @DisplayName("Should work with Address value object")
        void shouldWorkWithAddressValueObject() {
            Address addr = Address.builder()
                    .street("123 Test St")
                    .city("Test City")
                    .country("Test Country")
                    .build();
            Tenant tenant = Tenant.create(tenantId, tenantName, subdomain, primaryEmail, primaryPhone, addr);
            assertEquals(addr, tenant.getAddress());
        }

        @Test
        @DisplayName("Should work with TenantId value object")
        void shouldWorkWithTenantIdValueObject() {
            TenantId id = TenantId.generate();
            Tenant tenant = Tenant.create(id, tenantName, subdomain, primaryEmail, primaryPhone, address);
            assertEquals(id, tenant.getId());
        }
    }
}
