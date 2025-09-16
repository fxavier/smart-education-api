package com.xavier.smarteducationapi.tenant.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.xavier.smarteducationapi.common.domain.valueobject.Address;
import com.xavier.smarteducationapi.common.domain.valueobject.Email;
import com.xavier.smarteducationapi.common.domain.valueobject.Phone;
import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;
import com.xavier.smarteducationapi.tenant.domain.entity.Tenant;
import com.xavier.smarteducationapi.tenant.domain.repository.TenantRepository;
import com.xavier.smarteducationapi.tenant.domain.valueobject.TenantStatus;
import com.xavier.smarteducationapi.tenant.infrastructure.persistence.mapper.TenantMapper;
import com.xavier.smarteducationapi.tenant.infrastructure.persistence.repository.TenantJpaRepository;
import com.xavier.smarteducationapi.tenant.infrastructure.persistence.repository.TenantRepositoryImpl;

/**
 * Integration tests for the Tenant repository.
 * 
 * Tests the complete persistence layer including:
 * - JPA repository operations
 * - Domain to JPA entity mapping
 * - Query methods
 * - Data integrity
 * 
 * @author Xavier Nhagumbe
 */
@DataJpaTest(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.jpa.show-sql=false",
    "spring.jpa.defer-datasource-initialization=false",
    "spring.flyway.enabled=false",
    "spring.liquibase.enabled=false"
})
@Import({TenantRepositoryImpl.class, TenantMapper.class})
@DisplayName("Tenant Repository Integration Tests")
class TenantRepositoryIntegrationTests {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TenantJpaRepository tenantJpaRepository;
    
    @Autowired
    private TestEntityManager entityManager;

    private Tenant testTenant;
    private TenantId tenantId;
    private String subdomain;

    @BeforeEach
    @Transactional
    void setUp() {
        // Clean up any existing data
        tenantJpaRepository.deleteAll();
        entityManager.flush();

        // Create test data
        tenantId = TenantId.generate();
        subdomain = "test-school-" + System.currentTimeMillis(); // Ensure uniqueness
        
        testTenant = Tenant.create(
                tenantId,
                "Test School",
                subdomain,
                new Email("admin@test-school.edu"),
                new Phone("+1-234-567-8900"),
                Address.builder()
                        .street("123 Education St")
                        .city("Education City")
                        .country("Test Country")
                        .build()
        );
        testTenant.markEventsAsCommitted(); // Clear events for cleaner tests
    }

    @Test
    @DisplayName("Should save and retrieve tenant")
    void shouldSaveAndRetrieveTenant() {
        // When
        Tenant savedTenant = tenantRepository.save(testTenant);

        // Then
        assertNotNull(savedTenant);
        assertNotNull(savedTenant.getId());
        assertEquals(testTenant.getName(), savedTenant.getName());
        assertEquals(testTenant.getSubdomain(), savedTenant.getSubdomain());
        assertEquals(testTenant.getStatus(), savedTenant.getStatus());
        
        // Verify we can retrieve it
        Optional<Tenant> retrievedTenant = tenantRepository.findById(tenantId);
        assertTrue(retrievedTenant.isPresent());
        assertEquals(testTenant.getName(), retrievedTenant.get().getName());
    }

    @Test
    @DisplayName("Should find tenant by subdomain")
    void shouldFindTenantBySubdomain() {
        // Given
        tenantRepository.save(testTenant);

        // When
        Optional<Tenant> foundTenant = tenantRepository.findBySubdomain(subdomain);

        // Then
        assertTrue(foundTenant.isPresent());
        assertEquals(testTenant.getName(), foundTenant.get().getName());
        assertEquals(subdomain, foundTenant.get().getSubdomain());
    }

    @Test
    @DisplayName("Should return empty when subdomain not found")
    void shouldReturnEmptyWhenSubdomainNotFound() {
        // When
        Optional<Tenant> foundTenant = tenantRepository.findBySubdomain("non-existent-subdomain");

        // Then
        assertFalse(foundTenant.isPresent());
    }

    @Test
    @DisplayName("Should find tenants by status")
    void shouldFindTenantsByStatus() {
        // Given
        Tenant activeTenant = Tenant.create(
                TenantId.generate(),
                "Active School",
                "active-school-" + System.currentTimeMillis(),
                new Email("admin@active-school.edu"),
                new Phone("+1-987-654-3210"),
                Address.builder()
                        .street("456 Active St")
                        .city("Active City")
                        .country("Test Country")
                        .build()
        );
        activeTenant.activate();
        activeTenant.markEventsAsCommitted();

        tenantRepository.save(testTenant); // PENDING status
        tenantRepository.save(activeTenant); // ACTIVE status

        // When
        List<Tenant> pendingTenants = tenantRepository.findByStatus(TenantStatus.PENDING);
        List<Tenant> activeTenants = tenantRepository.findByStatus(TenantStatus.ACTIVE);

        // Then
        assertEquals(1, pendingTenants.size());
        assertEquals(TenantStatus.PENDING, pendingTenants.get(0).getStatus());

        assertEquals(1, activeTenants.size());
        assertEquals(TenantStatus.ACTIVE, activeTenants.get(0).getStatus());
    }

    @Test
    @DisplayName("Should check if subdomain exists")
    void shouldCheckIfSubdomainExists() {
        // Given
        tenantRepository.save(testTenant);

        // When & Then
        assertTrue(tenantRepository.existsBySubdomain(subdomain));
        assertFalse(tenantRepository.existsBySubdomain("non-existent-subdomain"));
    }

    @Test
    @DisplayName("Should check if email exists")
    void shouldCheckIfEmailExists() {
        // Given
        tenantRepository.save(testTenant);
        String email = testTenant.getPrimaryEmail().getValue();

        // When & Then
        assertTrue(tenantRepository.existsByEmail(email));
        assertFalse(tenantRepository.existsByEmail("non-existent@example.com"));
    }

    @Test
    @DisplayName("Should update tenant")
    void shouldUpdateTenant() {
        // Given
        Tenant savedTenant = tenantRepository.save(testTenant);
        savedTenant.activate();

        // When
        Tenant updatedTenant = tenantRepository.save(savedTenant);

        // Then
        assertEquals(TenantStatus.ACTIVE, updatedTenant.getStatus());
        assertNotNull(updatedTenant.getActivatedAt());

        // Verify persistence
        Optional<Tenant> retrievedTenant = tenantRepository.findById(tenantId);
        assertTrue(retrievedTenant.isPresent());
        assertEquals(TenantStatus.ACTIVE, retrievedTenant.get().getStatus());
    }

    @Test
    @DisplayName("Should delete tenant")
    void shouldDeleteTenant() {
        // Given
        tenantRepository.save(testTenant);
        assertTrue(tenantRepository.findById(tenantId).isPresent());

        // When
        tenantRepository.delete(testTenant);

        // Then
        assertFalse(tenantRepository.findById(tenantId).isPresent());
    }

    @Test
    @DisplayName("Should find all tenants")
    void shouldFindAllTenants() {
        // Given
        Tenant anotherTenant = Tenant.create(
                TenantId.generate(),
                "Another School",
                "another-school-" + System.currentTimeMillis(),
                new Email("admin@another-school.edu"),
                new Phone("+1-555-555-5555"),
                Address.builder()
                        .street("789 Another St")
                        .city("Another City")
                        .country("Test Country")
                        .build()
        );

        tenantRepository.save(testTenant);
        tenantRepository.save(anotherTenant);

        // When
        List<Tenant> allTenants = tenantRepository.findAll();

        // Then
        assertEquals(2, allTenants.size());
    }

    @Test
    @DisplayName("Should count tenants")
    void shouldCountTenants() {
        // Given
        assertEquals(0, tenantRepository.count());

        tenantRepository.save(testTenant);

        // When
        long count = tenantRepository.count();

        // Then
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Should preserve complex data through persistence")
    void shouldPreserveComplexDataThroughPersistence() {
        // Given
        testTenant.activate();
        testTenant.enableFeature("FEATURE_A");
        testTenant.enableFeature("FEATURE_B");
        testTenant.setBusinessRegistration("TAX123", "REG456");
        testTenant.updateLimits(50, 500);
        testTenant.markEventsAsCommitted();

        // When
        Tenant savedTenant = tenantRepository.save(testTenant);
        Optional<Tenant> retrievedTenant = tenantRepository.findById(tenantId);

        // Then
        assertTrue(retrievedTenant.isPresent());
        Tenant tenant = retrievedTenant.get();

        assertEquals(TenantStatus.ACTIVE, tenant.getStatus());
        assertTrue(tenant.hasFeature("FEATURE_A"));
        assertTrue(tenant.hasFeature("FEATURE_B"));
        assertEquals("TAX123", tenant.getTaxId());
        assertEquals("REG456", tenant.getRegistrationNumber());
        assertEquals(Integer.valueOf(50), tenant.getMaxUsers());
        assertEquals(Integer.valueOf(500), tenant.getMaxStudents());
        assertNotNull(tenant.getActivatedAt());
    }

    @Test
    @DisplayName("Should handle suspension and reactivation persistence")
    void shouldHandleSuspensionAndReactivationPersistence() {
        // Given
        testTenant.activate();
        testTenant.suspend("Testing suspension");
        testTenant.markEventsAsCommitted();

        // When
        tenantRepository.save(testTenant);
        Optional<Tenant> retrievedTenant = tenantRepository.findById(tenantId);

        // Then
        assertTrue(retrievedTenant.isPresent());
        Tenant tenant = retrievedTenant.get();

        assertEquals(TenantStatus.SUSPENDED, tenant.getStatus());
        assertEquals("Testing suspension", tenant.getSuspensionReason());
        assertNotNull(tenant.getSuspendedAt());

        // Test reactivation
        tenant.reactivate();
        tenantRepository.save(tenant);
        
        Optional<Tenant> reactivatedTenant = tenantRepository.findById(tenantId);
        assertTrue(reactivatedTenant.isPresent());
        assertEquals(TenantStatus.ACTIVE, reactivatedTenant.get().getStatus());
        assertNull(reactivatedTenant.get().getSuspensionReason());
        assertNull(reactivatedTenant.get().getSuspendedAt());
    }

    @Test
    @DisplayName("Should maintain data integrity across updates")
    void shouldMaintainDataIntegrityAcrossUpdates() {
        // Given
        Tenant savedTenant = tenantRepository.save(testTenant);
        
        // When - Make several updates
        savedTenant.activate();
        tenantRepository.save(savedTenant);
        
        savedTenant.enableFeature("TEST_FEATURE");
        tenantRepository.save(savedTenant);
        
        savedTenant.updateLimits(25, 250);
        tenantRepository.save(savedTenant);

        // Then - Verify final state
        Optional<Tenant> finalTenant = tenantRepository.findById(tenantId);
        assertTrue(finalTenant.isPresent());
        
        Tenant tenant = finalTenant.get();
        assertEquals(TenantStatus.ACTIVE, tenant.getStatus());
        assertTrue(tenant.hasFeature("TEST_FEATURE"));
        assertEquals(Integer.valueOf(25), tenant.getMaxUsers());
        assertEquals(Integer.valueOf(250), tenant.getMaxStudents());
        assertNotNull(tenant.getActivatedAt());
    }
}
