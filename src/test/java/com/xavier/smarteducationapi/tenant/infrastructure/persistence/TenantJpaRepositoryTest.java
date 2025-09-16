package com.xavier.smarteducationapi.tenant.infrastructure.persistence;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity.AddressEmbeddable;
import com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity.TenantJpaEntity;
import com.xavier.smarteducationapi.tenant.infrastructure.persistence.repository.TenantJpaRepository;

/**
 * Focused JPA integration tests for TenantJpaRepository.
 * Tests only the JPA layer without full Spring context.
 * 
 * @author Xavier Nhagumbe
 */
@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.show-sql=false",
    "spring.flyway.enabled=false",
    "spring.liquibase.enabled=false"
})
@org.springframework.test.context.ContextConfiguration(classes = TenantRepositoryTestConfiguration.class)
@DisplayName("Tenant JPA Repository Tests")
class TenantJpaRepositoryTest {

    @Autowired
    private TenantJpaRepository tenantJpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private TenantJpaEntity testTenant;

    @BeforeEach
    void setUp() {
        // Clean up any existing data
        tenantJpaRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        // Create test data
        Set<String> features = new HashSet<>();
        features.add("BASIC_PLAN");

        AddressEmbeddable address = AddressEmbeddable.builder()
                .street("123 Test Street")
                .city("Test City")
                .country("Test Country")
                .build();

        testTenant = TenantJpaEntity.builder()
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
    @DisplayName("Should save and find tenant by ID")
    void shouldSaveAndFindTenantById() {
        // When
        TenantJpaEntity saved = tenantJpaRepository.save(testTenant);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<TenantJpaEntity> found = tenantJpaRepository.findById("test-tenant-id");
        assertTrue(found.isPresent());
        assertEquals("Test School", found.get().getName());
        assertEquals("test-school", found.get().getSubdomain());
        assertEquals("PENDING", found.get().getStatus());
    }

    @Test
    @DisplayName("Should find tenant by subdomain")
    void shouldFindTenantBySubdomain() {
        // Given
        tenantJpaRepository.save(testTenant);
        entityManager.flush();
        entityManager.clear();

        // When
        Optional<TenantJpaEntity> found = tenantJpaRepository.findBySubdomain("test-school");

        // Then
        assertTrue(found.isPresent());
        assertEquals("Test School", found.get().getName());
    }

    @Test
    @DisplayName("Should find tenants by status")
    void shouldFindTenantsByStatus() {
        // Given
        TenantJpaEntity activeTenant = TenantJpaEntity.builder()
                .id("active-tenant")
                .name("Active School")
                .subdomain("active-school")
                .status("ACTIVE")
                .primaryEmail("admin@active-school.edu")
                .primaryPhone("+1-987-654-3210")
                .address(testTenant.getAddress())
                .features(new HashSet<>())
                .maxUsers(20)
                .maxStudents(200)
                .createdAt(Instant.now())
                .activatedAt(Instant.now())
                .version(0L)
                .build();

        tenantJpaRepository.save(testTenant); // PENDING
        tenantJpaRepository.save(activeTenant); // ACTIVE
        entityManager.flush();
        entityManager.clear();

        // When
        List<TenantJpaEntity> pendingTenants = tenantJpaRepository.findByStatus("PENDING");
        List<TenantJpaEntity> activeTenants = tenantJpaRepository.findByStatus("ACTIVE");

        // Then
        assertEquals(1, pendingTenants.size());
        assertEquals("PENDING", pendingTenants.get(0).getStatus());

        assertEquals(1, activeTenants.size());
        assertEquals("ACTIVE", activeTenants.get(0).getStatus());
    }

    @Test
    @DisplayName("Should check if subdomain exists")
    void shouldCheckIfSubdomainExists() {
        // Given
        tenantJpaRepository.save(testTenant);
        entityManager.flush();

        // When & Then
        assertTrue(tenantJpaRepository.existsBySubdomain("test-school"));
        assertFalse(tenantJpaRepository.existsBySubdomain("non-existent"));
    }

    @Test
    @DisplayName("Should check if email exists")
    void shouldCheckIfEmailExists() {
        // Given
        tenantJpaRepository.save(testTenant);
        entityManager.flush();

        // When & Then
        assertTrue(tenantJpaRepository.existsByPrimaryEmail("admin@test-school.edu"));
        assertFalse(tenantJpaRepository.existsByPrimaryEmail("non-existent@example.com"));
    }

    @Test
    @DisplayName("Should update tenant status")
    void shouldUpdateTenantStatus() {
        // Given
        TenantJpaEntity saved = tenantJpaRepository.save(testTenant);
        entityManager.flush();

        // When
        saved.setStatus("ACTIVE");
        saved.setActivatedAt(Instant.now());
        TenantJpaEntity updated = tenantJpaRepository.save(saved);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<TenantJpaEntity> retrieved = tenantJpaRepository.findById("test-tenant-id");
        assertTrue(retrieved.isPresent());
        assertEquals("ACTIVE", retrieved.get().getStatus());
        assertNotNull(retrieved.get().getActivatedAt());
    }

    @Test
    @DisplayName("Should delete tenant")
    void shouldDeleteTenant() {
        // Given
        tenantJpaRepository.save(testTenant);
        entityManager.flush();
        assertTrue(tenantJpaRepository.existsById("test-tenant-id"));

        // When
        tenantJpaRepository.deleteById("test-tenant-id");
        entityManager.flush();

        // Then
        assertFalse(tenantJpaRepository.existsById("test-tenant-id"));
    }

    @Test
    @DisplayName("Should count tenants")
    void shouldCountTenants() {
        // Given
        assertEquals(0, tenantJpaRepository.count());

        // When
        tenantJpaRepository.save(testTenant);
        entityManager.flush();

        // Then
        assertEquals(1, tenantJpaRepository.count());
    }

    @Test
    @DisplayName("Should preserve complex data")
    void shouldPreserveComplexData() {
        // Given
        Set<String> features = new HashSet<>();
        features.add("PREMIUM_PLAN");
        features.add("ANALYTICS");
        features.add("API_ACCESS");

        testTenant.setFeatures(features);
        testTenant.setTaxId("TAX123456");
        testTenant.setRegistrationNumber("REG789012");
        testTenant.setMaxUsers(50);
        testTenant.setMaxStudents(500);

        // When
        tenantJpaRepository.save(testTenant);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<TenantJpaEntity> retrieved = tenantJpaRepository.findById("test-tenant-id");
        assertTrue(retrieved.isPresent());

        TenantJpaEntity tenant = retrieved.get();
        assertEquals(3, tenant.getFeatures().size());
        assertTrue(tenant.getFeatures().contains("PREMIUM_PLAN"));
        assertTrue(tenant.getFeatures().contains("ANALYTICS"));
        assertTrue(tenant.getFeatures().contains("API_ACCESS"));
        assertEquals("TAX123456", tenant.getTaxId());
        assertEquals("REG789012", tenant.getRegistrationNumber());
        assertEquals(Integer.valueOf(50), tenant.getMaxUsers());
        assertEquals(Integer.valueOf(500), tenant.getMaxStudents());
    }

    @Test
    @DisplayName("Should handle suspension data")
    void shouldHandleSuspensionData() {
        // Given
        testTenant.setStatus("SUSPENDED");
        testTenant.setSuspendedAt(Instant.now());
        testTenant.setSuspensionReason("Testing suspension");

        // When
        tenantJpaRepository.save(testTenant);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<TenantJpaEntity> retrieved = tenantJpaRepository.findById("test-tenant-id");
        assertTrue(retrieved.isPresent());

        TenantJpaEntity tenant = retrieved.get();
        assertEquals("SUSPENDED", tenant.getStatus());
        assertNotNull(tenant.getSuspendedAt());
        assertEquals("Testing suspension", tenant.getSuspensionReason());
    }

    @Test
    @DisplayName("Should find all active tenants")
    void shouldFindAllActiveTenants() {
        // Given
        testTenant.setStatus("ACTIVE");
        testTenant.setActivatedAt(Instant.now());

        TenantJpaEntity suspendedTenant = TenantJpaEntity.builder()
                .id("suspended-tenant")
                .name("Suspended School")
                .subdomain("suspended-school")
                .status("SUSPENDED")
                .primaryEmail("admin@suspended-school.edu")
                .primaryPhone("+1-555-555-5555")
                .address(testTenant.getAddress())
                .features(new HashSet<>())
                .maxUsers(10)
                .maxStudents(100)
                .createdAt(Instant.now())
                .suspendedAt(Instant.now())
                .version(0L)
                .build();

        tenantJpaRepository.save(testTenant);
        tenantJpaRepository.save(suspendedTenant);
        entityManager.flush();

        // When
        List<TenantJpaEntity> activeTenants = tenantJpaRepository.findAllActiveTenants();

        // Then
        assertEquals(1, activeTenants.size());
        assertEquals("ACTIVE", activeTenants.get(0).getStatus());
        assertEquals("Test School", activeTenants.get(0).getName());
    }

    @Test
    @DisplayName("Should count tenants by status")
    void shouldCountTenantsByStatus() {
        // Given
        testTenant.setStatus("ACTIVE");
        TenantJpaEntity pendingTenant = TenantJpaEntity.builder()
                .id("pending-tenant")
                .name("Pending School")
                .subdomain("pending-school")
                .status("PENDING")
                .primaryEmail("admin@pending-school.edu")
                .primaryPhone("+1-777-777-7777")
                .address(testTenant.getAddress())
                .features(new HashSet<>())
                .maxUsers(10)
                .maxStudents(100)
                .createdAt(Instant.now())
                .version(0L)
                .build();

        tenantJpaRepository.save(testTenant);
        tenantJpaRepository.save(pendingTenant);
        entityManager.flush();

        // When
        long activeCount = tenantJpaRepository.countByStatus("ACTIVE");
        long pendingCount = tenantJpaRepository.countByStatus("PENDING");

        // Then
        assertEquals(1, activeCount);
        assertEquals(1, pendingCount);
    }
}
