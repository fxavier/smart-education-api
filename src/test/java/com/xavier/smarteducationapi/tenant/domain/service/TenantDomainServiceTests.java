package com.xavier.smarteducationapi.tenant.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.xavier.smarteducationapi.common.domain.exception.BusinessRuleViolationException;
import com.xavier.smarteducationapi.common.domain.valueobject.Address;
import com.xavier.smarteducationapi.common.domain.valueobject.Email;
import com.xavier.smarteducationapi.common.domain.valueobject.Phone;
import com.xavier.smarteducationapi.tenant.domain.entity.Tenant;
import com.xavier.smarteducationapi.tenant.domain.repository.TenantRepository;
import com.xavier.smarteducationapi.tenant.domain.valueobject.TenantStatus;

/**
 * Unit tests for the TenantDomainService.
 * 
 * Tests domain-level business logic including:
 * - Tenant creation validation
 * - Uniqueness constraints
 * - Business rule enforcement
 * 
 * @author Xavier Nhagumbe
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tenant Domain Service Tests")
class TenantDomainServiceTests {

    @Mock
    private TenantRepository tenantRepository;

    @InjectMocks
    private TenantDomainService tenantDomainService;

    private Email primaryEmail;
    private Phone primaryPhone;
    private Address address;

    @BeforeEach
    void setUp() {
        primaryEmail = new Email("admin@test-school.edu");
        primaryPhone = new Phone("+1-234-567-8900");
        address = Address.builder()
                .street("123 Education St")
                .city("Education City")
                .country("Test Country")
                .build();
    }

    @Test
    @DisplayName("Should create tenant with unique subdomain and email")
    void shouldCreateTenantWithUniqueSubdomainAndEmail() {
        // Given
        String name = "Test School";
        String subdomain = "test-school";

        when(tenantRepository.existsBySubdomain(subdomain)).thenReturn(false);
        when(tenantRepository.existsByEmail(primaryEmail.getValue())).thenReturn(false);
        when(tenantRepository.save(any(Tenant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Tenant result = tenantDomainService.createTenant(name, subdomain, primaryEmail, primaryPhone, address);

        // Then
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(subdomain, result.getSubdomain());
        assertEquals(primaryEmail, result.getPrimaryEmail());
        assertEquals(primaryPhone, result.getPrimaryPhone());
        assertEquals(address, result.getAddress());
        assertEquals(TenantStatus.PENDING, result.getStatus());

        verify(tenantRepository).existsBySubdomain(subdomain);
        verify(tenantRepository).existsByEmail(primaryEmail.getValue());
        verify(tenantRepository).save(any(Tenant.class));
    }

    @Test
    @DisplayName("Should throw exception for duplicate subdomain")
    void shouldThrowExceptionForDuplicateSubdomain() {
        // Given
        String subdomain = "existing-subdomain";

        when(tenantRepository.existsBySubdomain(subdomain)).thenReturn(true);

        // When & Then
        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tenantDomainService.validateUniqueSubdomain(subdomain)
        );

        assertEquals("UniqueSubdomain", exception.getRuleName());
        assertTrue(exception.getMessage().contains("Subdomain already exists"));
        assertEquals(subdomain, exception.getViolatingValue());
    }

    @Test
    @DisplayName("Should throw exception for duplicate email")
    void shouldThrowExceptionForDuplicateEmail() {
        // Given
        String email = "existing@school.edu";

        when(tenantRepository.existsByEmail(email)).thenReturn(true);

        // When & Then
        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tenantDomainService.validateUniqueEmail(email)
        );

        assertEquals("UniqueEmail", exception.getRuleName());
        assertTrue(exception.getMessage().contains("Email already registered"));
        assertEquals(email, exception.getViolatingValue());
    }

    @Test
    @DisplayName("Should throw exception when creating tenant with existing subdomain")
    void shouldThrowExceptionWhenCreatingTenantWithExistingSubdomain() {
        // Given
        String name = "Test School";
        String subdomain = "existing-subdomain";

        when(tenantRepository.existsBySubdomain(subdomain)).thenReturn(true);

        // When & Then
        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tenantDomainService.createTenant(name, subdomain, primaryEmail, primaryPhone, address)
        );

        assertEquals("UniqueSubdomain", exception.getRuleName());
    }

    @Test
    @DisplayName("Should throw exception when creating tenant with existing email")
    void shouldThrowExceptionWhenCreatingTenantWithExistingEmail() {
        // Given
        String name = "Test School";
        String subdomain = "new-subdomain";

        when(tenantRepository.existsBySubdomain(subdomain)).thenReturn(false);
        when(tenantRepository.existsByEmail(primaryEmail.getValue())).thenReturn(true);

        // When & Then
        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tenantDomainService.createTenant(name, subdomain, primaryEmail, primaryPhone, address)
        );

        assertEquals("UniqueEmail", exception.getRuleName());
    }

    @Test
    @DisplayName("Should validate unique subdomain successfully")
    void shouldValidateUniqueSubdomainSuccessfully() {
        // Given
        String subdomain = "unique-subdomain";

        when(tenantRepository.existsBySubdomain(subdomain)).thenReturn(false);

        // When & Then - Should not throw exception
        tenantDomainService.validateUniqueSubdomain(subdomain);

        verify(tenantRepository).existsBySubdomain(subdomain);
    }

    @Test
    @DisplayName("Should validate unique email successfully")
    void shouldValidateUniqueEmailSuccessfully() {
        // Given
        String email = "unique@school.edu";

        when(tenantRepository.existsByEmail(email)).thenReturn(false);

        // When & Then - Should not throw exception
        tenantDomainService.validateUniqueEmail(email);

        verify(tenantRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("Should create tenant with all required domain events")
    void shouldCreateTenantWithAllRequiredDomainEvents() {
        // Given
        String name = "Test School";
        String subdomain = "test-school";

        when(tenantRepository.existsBySubdomain(subdomain)).thenReturn(false);
        when(tenantRepository.existsByEmail(primaryEmail.getValue())).thenReturn(false);
        when(tenantRepository.save(any(Tenant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Tenant result = tenantDomainService.createTenant(name, subdomain, primaryEmail, primaryPhone, address);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getUncommittedEvents().size());
        assertEquals("tenant.created", result.getUncommittedEvents().get(0).topic());
    }
}
