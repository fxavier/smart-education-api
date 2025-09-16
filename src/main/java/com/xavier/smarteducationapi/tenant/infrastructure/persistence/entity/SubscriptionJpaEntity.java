package com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * JPA entity for Subscription.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Entity
@Table(name = "subscriptions", indexes = {
        @Index(name = "idx_subscription_tenant", columnList = "tenant_id"),
        @Index(name = "idx_subscription_status", columnList = "status"),
        @Index(name = "idx_subscription_billing_date", columnList = "next_billing_date"),
        @Index(name = "idx_subscription_end_date", columnList = "end_date")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionJpaEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 36)
    private String tenantId;

    @Column(name = "plan", nullable = false, length = 20)
    private String plan;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "next_billing_date")
    private LocalDate nextBillingDate;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "billing_period", nullable = false, length = 20)
    private String billingPeriod;

    @Column(name = "user_limit")
    private Integer userLimit;

    @Column(name = "student_limit")
    private Integer studentLimit;

    @Column(name = "storage_limit")
    private Integer storageLimit;

    @Column(name = "trial_end_date")
    private LocalDate trialEndDate;

    @Column(name = "auto_renew", nullable = false)
    @Builder.Default
    private boolean autoRenew = true;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}