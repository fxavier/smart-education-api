package com.xavier.smarteducationapi.tenant.application;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.xavier.smarteducationapi.common.domain.exception.EntityNotFoundException;
import com.xavier.smarteducationapi.common.domain.valueobject.Address;
import com.xavier.smarteducationapi.common.domain.valueobject.Email;
import com.xavier.smarteducationapi.common.domain.valueobject.Phone;
import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;
import com.xavier.smarteducationapi.tenant.application.dto.TenantDto;
import com.xavier.smarteducationapi.tenant.application.service.TenantApplicationService;
import com.xavier.smarteducationapi.tenant.domain.entity.Tenant;
import com.xavier.smarteducationapi.tenant.domain.repository.TenantRepository;
import com.xavier.smarteducationapi.tenant.domain.service.TenantDomainService;
import com.xavier.smarteducationapi.tenant.domain.valueobject.TenantStatus;

/**
 * Unit tests for the TenantApplicationService.
 * 
 * Tests the application service layer which orchestrates
 * domain operations, handles commands, and publishes events.
 * 
 * @author Xavier Nhagumbe
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tenant Application Service Tests")
class TenantApplicationServiceTests {

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private TenantDomainService tenantDomainService;

    @InjectMocks
    private TenantApplicationService tenantApplicationService;

    private TenantId tenantId;
    private Tenant testTenant;

    @BeforeEach
    void setUp() {
        tenantId = TenantId.generate();
        testTenant = createTestTenant();
    }

    private Tenant createTestTenant() {
        return Tenant.create(
                tenantId,
                "Test School",
                "test-school",
                new Email("admin@test-school.edu"),
                new Phone("+1-234-567-8900"),
                Address.builder()
                        .street("123 Education St")
                        .city("Education City")
                        .country("Test Country")
                        .build()
        );
    }

    @Test
    @DisplayName("Should get tenant by ID")
    void shouldGetTenantById() {
        // Given
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(testTenant));

        // When
        TenantDto result = tenantApplicationService.getTenantById(tenantId.toString());

        // Then
        assertNotNull(result);
        assertEquals(testTenant.getName(), result.getName());
        assertEquals(testTenant.getSubdomain(), result.getSubdomain());
        assertEquals(testTenant.getPrimaryEmail().getValue(), result.getPrimaryEmail());
        assertEquals(TenantStatus.PENDING.name(), result.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when tenant not found by ID")
    void shouldThrowExceptionWhenTenantNotFoundById() {
        // Given
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> tenantApplicationService.getTenantById(tenantId.toString())
        );

        assertEquals("Tenant", exception.getEntityType());
        assertEquals(tenantId.toString(), exception.getEntityId());
    }

    @Test
    @DisplayName("Should get tenant by subdomain")
    void shouldGetTenantBySubdomain() {
        // Given
        String subdomain = "test-school";
        when(tenantRepository.findBySubdomain(subdomain)).thenReturn(Optional.of(testTenant));

        // When
        TenantDto result = tenantApplicationService.getTenantBySubdomain(subdomain);

        // Then
        assertNotNull(result);
        assertEquals(testTenant.getName(), result.getName());
        assertEquals(subdomain, result.getSubdomain());
    }

    @Test
    @DisplayName("Should throw exception when tenant not found by subdomain")
    void shouldThrowExceptionWhenTenantNotFoundBySubdomain() {
        // Given
        String subdomain = "non-existent";
        when(tenantRepository.findBySubdomain(subdomain)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> tenantApplicationService.getTenantBySubdomain(subdomain)
        );

        assertEquals("Tenant", exception.getEntityType());
        assertEquals(subdomain, exception.getEntityId());
    }

    @Test
    @DisplayName("Should activate tenant")
    void shouldActivateTenant() {
        // Given
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(testTenant));
        when(tenantRepository.save(any(Tenant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        TenantDto result = tenantApplicationService.activateTenant(tenantId.toString());

        // Then
        assertNotNull(result);
        assertEquals(TenantStatus.ACTIVE.name(), result.getStatus());
        assertNotNull(result.getActivatedAt());
        
        verify(tenantRepository).save(any(Tenant.class));
    }

    @Test
    @DisplayName("Should suspend tenant")
    void shouldSuspendTenant() {
        // Given
        testTenant.activate();
        testTenant.markEventsAsCommitted();
        
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(testTenant));
        when(tenantRepository.save(any(Tenant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String reason = "Payment overdue";

        // When
        TenantDto result = tenantApplicationService.suspendTenant(tenantId.toString(), reason);

        // Then
        assertNotNull(result);
        assertEquals(TenantStatus.SUSPENDED.name(), result.getStatus());
        
        verify(tenantRepository).save(any(Tenant.class));
    }
}
