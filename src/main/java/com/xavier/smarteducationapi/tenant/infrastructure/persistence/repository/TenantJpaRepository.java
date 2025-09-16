package com.xavier.smarteducationapi.tenant.infrastructure.persistence.repository;

import com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity.TenantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Tenant entities.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Repository
public interface TenantJpaRepository extends JpaRepository<TenantJpaEntity, String> {

    Optional<TenantJpaEntity> findBySubdomain(String subdomain);

    List<TenantJpaEntity> findByStatus(String status);

    boolean existsBySubdomain(String subdomain);

    boolean existsByPrimaryEmail(String primaryEmail);

    @Query("SELECT t FROM TenantJpaEntity t WHERE t.status = 'ACTIVE'")
    List<TenantJpaEntity> findAllActiveTenants();

    @Query("SELECT COUNT(t) FROM TenantJpaEntity t WHERE t.status = :status")
    long countByStatus(@Param("status") String status);
}