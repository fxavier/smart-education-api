package com.xavier.smarteducationapi.tenant.domain.service;

import com.xavier.smarteducationapi.common.domain.valueobject.*;
import com.xavier.smarteducationapi.tenant.domain.entity.Tenant;
import com.xavier.smarteducationapi.tenant.domain.repository.TenantRepository;
import com.xavier.smarteducationapi.common.domain.exception.BusinessRuleViolationException;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

/**
 * Domain service for tenant-related business logic.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Service
@RequiredArgsConstructor
public class TenantDomainService {

    private final TenantRepository tenantRepository;

    public void validateUniqueSubdomain(String subdomain) {
        if (tenantRepository.existsBySubdomain(subdomain)) {
            throw new BusinessRuleViolationException(
                    "UniqueSubdomain",
                    "Subdomain already exists",
                    subdomain
            );
        }
    }

    public void validateUniqueEmail(String email) {
        if (tenantRepository.existsByEmail(email)) {
            throw new BusinessRuleViolationException(
                    "UniqueEmail",
                    "Email already registered",
                    email
            );
        }
    }

    public Tenant createTenant(
            String name,
            String subdomain,
            Email primaryEmail,
            Phone primaryPhone,
            Address address) {

        // Validate uniqueness
        validateUniqueSubdomain(subdomain);
        validateUniqueEmail(primaryEmail.getValue());

        // Create tenant
        TenantId tenantId = TenantId.generate();
        Tenant tenant = Tenant.create(
                tenantId,
                name,
                subdomain,
                primaryEmail,
                primaryPhone,
                address
        );

        return tenantRepository.save(tenant);
    }
}