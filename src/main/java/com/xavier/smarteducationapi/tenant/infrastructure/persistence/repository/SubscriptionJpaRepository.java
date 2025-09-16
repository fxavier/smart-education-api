package com.xavier.smarteducationapi.tenant.infrastructure.persistence.repository;

import com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity.SubscriptionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Subscription entities.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Repository
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionJpaEntity, String> {

    @Query("SELECT s FROM SubscriptionJpaEntity s WHERE s.tenantId = :tenantId AND s.status = 'ACTIVE'")
    Optional<SubscriptionJpaEntity> findActiveByTenantId(@Param("tenantId") String tenantId);

    List<SubscriptionJpaEntity> findByTenantId(String tenantId);

    List<SubscriptionJpaEntity> findByStatus(String status);

    @Query("SELECT s FROM SubscriptionJpaEntity s WHERE s.endDate < :date AND s.status = 'ACTIVE'")
    List<SubscriptionJpaEntity> findExpiringBefore(@Param("date") LocalDate date);

    List<SubscriptionJpaEntity> findByNextBillingDate(LocalDate date);

    @Query("SELECT COUNT(s) FROM SubscriptionJpaEntity s WHERE s.status = 'ACTIVE'")
    long countActiveSubscriptions();
}