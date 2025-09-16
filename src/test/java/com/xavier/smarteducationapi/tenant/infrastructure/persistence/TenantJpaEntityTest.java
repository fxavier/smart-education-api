package com.xavier.smarteducationapi.tenant.infrastructure.persistence;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity.AddressEmbeddable;
import com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity.TenantJpaEntity;

/**
 * Unit tests for TenantJpaEntity without Spring context.
 * Tests the JPA entity structure and builder pattern.
 * 
 * @author Xavier Nhagumbe
 */
@DisplayName("Tenant JPA Entity Unit Tests")
class TenantJpaEntityTest {

    private TenantJpaEntity tenantEntity;
    private AddressEmbeddable address;
    private Set<String> features;

    @BeforeEach
    void setUp() {
        features = new HashSet<>();
        features.add("BASIC_PLAN");
        features.add("EMAIL_SUPPORT");

        address = AddressEmbeddable.builder()
                .street("123 Test Street")
                .city("Test City")
                .country("Test Country")
                .build();

        tenantEntity = TenantJpaEntity.builder()
                .id("test-tenant-id")
                .name("Test School")
                .subdomain("test-school")
                .status("PENDING")
                .primaryEmail("admin@test-school.edu")
                .primaryPhone("+1-234-567-8900")
                .address(address)
                .features(features)
                .maxUsers(10)
                .maxStudents(100)
                .createdAt(Instant.now())
                .version(0L)
                .build();
    }

    @Test
    @DisplayName("Should create tenant entity with all required fields")
    void shouldCreateTenantEntityWithAllRequiredFields() {
        assertNotNull(tenantEntity);
        assertEquals("test-tenant-id", tenantEntity.getId());
        assertEquals("Test School", tenantEntity.getName());
        assertEquals("test-school", tenantEntity.getSubdomain());
        assertEquals("PENDING", tenantEntity.getStatus());
        assertEquals("admin@test-school.edu", tenantEntity.getPrimaryEmail());
        assertEquals("+1-234-567-8900", tenantEntity.getPrimaryPhone());
        assertEquals(Integer.valueOf(10), tenantEntity.getMaxUsers());
        assertEquals(Integer.valueOf(100), tenantEntity.getMaxStudents());
        assertNotNull(tenantEntity.getCreatedAt());
        assertEquals(Long.valueOf(0L), tenantEntity.getVersion());
    }

    @Test
    @DisplayName("Should handle address embeddable correctly")
    void shouldHandleAddressEmbeddableCorrectly() {
        assertNotNull(tenantEntity.getAddress());
        assertEquals("123 Test Street", tenantEntity.getAddress().getStreet());
        assertEquals("Test City", tenantEntity.getAddress().getCity());
        assertEquals("Test Country", tenantEntity.getAddress().getCountry());
    }

    @Test
    @DisplayName("Should handle features collection correctly")
    void shouldHandleFeaturesCollectionCorrectly() {
        assertNotNull(tenantEntity.getFeatures());
        assertEquals(2, tenantEntity.getFeatures().size());
        assertTrue(tenantEntity.getFeatures().contains("BASIC_PLAN"));
        assertTrue(tenantEntity.getFeatures().contains("EMAIL_SUPPORT"));
    }

    @Test
    @DisplayName("Should handle optional fields correctly")
    void shouldHandleOptionalFieldsCorrectly() {
        // Given - entity with optional fields
        Instant activatedTime = Instant.now();
        Instant suspendedTime = Instant.now().plusSeconds(3600);
        
        tenantEntity.setStatus("SUSPENDED");
        tenantEntity.setActivatedAt(activatedTime);
        tenantEntity.setSuspendedAt(suspendedTime);
        tenantEntity.setSuspensionReason("Testing suspension");
        tenantEntity.setTaxId("TAX123456");
        tenantEntity.setRegistrationNumber("REG789012");

        // Then
        assertEquals("SUSPENDED", tenantEntity.getStatus());
        assertEquals(activatedTime, tenantEntity.getActivatedAt());
        assertEquals(suspendedTime, tenantEntity.getSuspendedAt());
        assertEquals("Testing suspension", tenantEntity.getSuspensionReason());
        assertEquals("TAX123456", tenantEntity.getTaxId());
        assertEquals("REG789012", tenantEntity.getRegistrationNumber());
    }

    @Test
    @DisplayName("Should support builder pattern modification")
    void shouldSupportBuilderPatternModification() {
        // Given - modify existing entity using builder
        Set<String> newFeatures = new HashSet<>();
        newFeatures.add("PREMIUM_PLAN");
        newFeatures.add("PHONE_SUPPORT");
        newFeatures.add("API_ACCESS");

        TenantJpaEntity modifiedEntity = TenantJpaEntity.builder()
                .id(tenantEntity.getId())
                .name(tenantEntity.getName())
                .subdomain(tenantEntity.getSubdomain())
                .status("ACTIVE")
                .primaryEmail(tenantEntity.getPrimaryEmail())
                .primaryPhone(tenantEntity.getPrimaryPhone())
                .address(tenantEntity.getAddress())
                .activatedAt(Instant.now())
                .features(newFeatures)
                .maxUsers(50)
                .maxStudents(500)
                .createdAt(tenantEntity.getCreatedAt())
                .version(1L)
                .build();

        // Then
        assertEquals("ACTIVE", modifiedEntity.getStatus());
        assertNotNull(modifiedEntity.getActivatedAt());
        assertEquals(3, modifiedEntity.getFeatures().size());
        assertTrue(modifiedEntity.getFeatures().contains("PREMIUM_PLAN"));
        assertTrue(modifiedEntity.getFeatures().contains("PHONE_SUPPORT"));
        assertTrue(modifiedEntity.getFeatures().contains("API_ACCESS"));
        assertEquals(Integer.valueOf(50), modifiedEntity.getMaxUsers());
        assertEquals(Integer.valueOf(500), modifiedEntity.getMaxStudents());
        assertEquals(Long.valueOf(1L), modifiedEntity.getVersion());

        // Original entity should remain unchanged
        assertEquals("PENDING", tenantEntity.getStatus());
        assertEquals(2, tenantEntity.getFeatures().size());
        assertEquals(Integer.valueOf(10), tenantEntity.getMaxUsers());
    }

    @Test
    @DisplayName("Should handle empty features collection")
    void shouldHandleEmptyFeaturesCollection() {
        TenantJpaEntity entityWithEmptyFeatures = TenantJpaEntity.builder()
                .id("empty-features-tenant")
                .name("Empty Features School")
                .subdomain("empty-features-school")
                .status("PENDING")
                .primaryEmail("admin@empty.edu")
                .primaryPhone("+1-555-555-5555")
                .address(address)
                .features(new HashSet<>())
                .maxUsers(5)
                .maxStudents(50)
                .createdAt(Instant.now())
                .version(0L)
                .build();

        assertNotNull(entityWithEmptyFeatures.getFeatures());
        assertTrue(entityWithEmptyFeatures.getFeatures().isEmpty());
    }

    @Test
    @DisplayName("Should handle data type conversions correctly")
    void shouldHandleDataTypeConversionsCorrectly() {
        // Test that the entity handles different data types properly
        assertNotNull(tenantEntity.getCreatedAt());
        assertTrue(tenantEntity.getCreatedAt() instanceof Instant);
        
        assertNotNull(tenantEntity.getMaxUsers());
        assertTrue(tenantEntity.getMaxUsers() instanceof Integer);
        
        assertNotNull(tenantEntity.getMaxStudents());
        assertTrue(tenantEntity.getMaxStudents() instanceof Integer);
        
        assertNotNull(tenantEntity.getVersion());
        assertTrue(tenantEntity.getVersion() instanceof Long);
    }

    @Test
    @DisplayName("Should support all status values")
    void shouldSupportAllStatusValues() {
        // Test different status values
        tenantEntity.setStatus("PENDING");
        assertEquals("PENDING", tenantEntity.getStatus());
        
        tenantEntity.setStatus("ACTIVE");
        assertEquals("ACTIVE", tenantEntity.getStatus());
        
        tenantEntity.setStatus("SUSPENDED");
        assertEquals("SUSPENDED", tenantEntity.getStatus());
        
        tenantEntity.setStatus("CANCELLED");
        assertEquals("CANCELLED", tenantEntity.getStatus());
    }
}
