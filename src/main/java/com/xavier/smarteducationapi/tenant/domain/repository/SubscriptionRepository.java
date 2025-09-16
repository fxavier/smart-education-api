package com.xavier.smarteducationapi.tenant.domain.repository;

import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;
import com.xavier.smarteducationapi.tenant.domain.entity.Subscription;
import com.xavier.smarteducationapi.tenant.domain.valueobject.SubscriptionStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Subscription entity.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public interface SubscriptionRepository {

    Subscription save(Subscription subscription);

    Optional<Subscription> findById(UUID id);

    Optional<Subscription> findActiveByTenantId(TenantId tenantId);

    List<Subscription> findByTenantId(TenantId tenantId);

    List<Subscription> findByStatus(SubscriptionStatus status);

    List<Subscription> findExpiringBefore(LocalDate date);

    List<Subscription> findByNextBillingDate(LocalDate date);

    void delete(Subscription subscription);

    long countActiveSubscriptions();
}