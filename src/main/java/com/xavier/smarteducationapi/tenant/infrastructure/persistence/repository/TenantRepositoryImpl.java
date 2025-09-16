package com.xavier.smarteducationapi.tenant.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;
import com.xavier.smarteducationapi.tenant.domain.entity.Tenant;
import com.xavier.smarteducationapi.tenant.domain.repository.TenantRepository;
import com.xavier.smarteducationapi.tenant.domain.valueobject.TenantStatus;
import com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity.TenantJpaEntity;
import com.xavier.smarteducationapi.tenant.infrastructure.persistence.mapper.TenantMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of TenantRepository using JPA.
 * Adapts between domain and persistence layers.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Repository
@RequiredArgsConstructor
public class TenantRepositoryImpl implements TenantRepository {

    private final TenantJpaRepository jpaRepository;
    private final TenantMapper mapper;

    @Override
    public Tenant save(Tenant tenant) {
        TenantJpaEntity entity = mapper.toJpaEntity(tenant);
        TenantJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Tenant> findById(TenantId tenantId) {
        return jpaRepository.findById(tenantId.toString())
                .map(mapper::toDomainEntity);
    }

    @Override
    public Optional<Tenant> findBySubdomain(String subdomain) {
        return jpaRepository.findBySubdomain(subdomain)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Tenant> findByStatus(TenantStatus status) {
        return jpaRepository.findByStatus(status.name()).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsBySubdomain(String subdomain) {
        return jpaRepository.existsBySubdomain(subdomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByPrimaryEmail(email);
    }

    @Override
    public void delete(Tenant tenant) {
        jpaRepository.deleteById(tenant.getId().toString());
    }

    @Override
    public List<Tenant> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}