package com.xavier.smarteducationapi.tenant.infrastructure.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;
import com.xavier.smarteducationapi.tenant.domain.entity.Subscription;
import com.xavier.smarteducationapi.tenant.domain.repository.SubscriptionRepository;
import com.xavier.smarteducationapi.tenant.domain.valueobject.SubscriptionStatus;
import com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity.SubscriptionJpaEntity;
import com.xavier.smarteducationapi.tenant.infrastructure.persistence.mapper.SubscriptionMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of SubscriptionRepository using JPA.
 * Adapts between domain and persistence layers.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final SubscriptionJpaRepository jpaRepository;
    private final SubscriptionMapper mapper;

    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionJpaEntity entity = mapper.toJpaEntity(subscription);
        SubscriptionJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Subscription> findById(UUID id) {
        return jpaRepository.findById(id.toString())
                .map(mapper::toDomainEntity);
    }

    @Override
    public Optional<Subscription> findActiveByTenantId(TenantId tenantId) {
        return jpaRepository.findActiveByTenantId(tenantId.toString())
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Subscription> findByTenantId(TenantId tenantId) {
        return jpaRepository.findByTenantId(tenantId.toString()).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Subscription> findByStatus(SubscriptionStatus status) {
        return jpaRepository.findByStatus(status.name()).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Subscription> findExpiringBefore(LocalDate date) {
        return jpaRepository.findExpiringBefore(date).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Subscription> findByNextBillingDate(LocalDate date) {
        return jpaRepository.findByNextBillingDate(date).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Subscription subscription) {
        jpaRepository.deleteById(subscription.getId().toString());
    }

    @Override
    public long countActiveSubscriptions() {
        return jpaRepository.countActiveSubscriptions();
    }
}