package com.xavier.smarteducationapi.tenant.domain.repository;

import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;
import com.xavier.smarteducationapi.tenant.domain.entity.Tenant;
import com.xavier.smarteducationapi.tenant.domain.valueobject.TenantStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Tenant aggregate.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public interface TenantRepository {

    Tenant save(Tenant tenant);

    Optional<Tenant> findById(TenantId tenantId);

    Optional<Tenant> findBySubdomain(String subdomain);

    List<Tenant> findByStatus(TenantStatus status);

    boolean existsBySubdomain(String subdomain);

    boolean existsByEmail(String email);

    void delete(Tenant tenant);

    List<Tenant> findAll();

    long count();
}