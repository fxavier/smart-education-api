package com.xavier.smarteducationapi.tenant.infrastructure.persistence.mapper;


import com.xavier.smarteducationapi.common.domain.valueobject.Money;
import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;
import com.xavier.smarteducationapi.tenant.domain.entity.Subscription;
import com.xavier.smarteducationapi.tenant.domain.valueobject.*;
import com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity.SubscriptionJpaEntity;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Mapper for converting between Subscription domain and JPA entities.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Component
public class SubscriptionMapper {

    public SubscriptionJpaEntity toJpaEntity(Subscription subscription) {
        if (subscription == null) {
            return null;
        }

        return SubscriptionJpaEntity.builder()
                .id(subscription.getId().toString())
                .tenantId(subscription.getTenantId().toString())
                .plan(subscription.getPlan().name())
                .status(subscription.getStatus().name())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .nextBillingDate(subscription.getNextBillingDate())
                .price(subscription.getPrice().getAmount())
                .billingPeriod(subscription.getBillingPeriod().name())
                .userLimit(subscription.getUserLimit())
                .studentLimit(subscription.getStudentLimit())
                .storageLimit(subscription.getStorageLimit())
                .trialEndDate(subscription.getTrialEndDate())
                .autoRenew(subscription.isAutoRenew())
                .createdAt(subscription.getCreatedAt())
                .updatedAt(subscription.getUpdatedAt())
                .build();
    }

    public Subscription toDomainEntity(SubscriptionJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        // Use the reconstruct method
        return Subscription.reconstruct(
                UUID.fromString(entity.getId()),
                TenantId.of(entity.getTenantId()),
                SubscriptionPlan.valueOf(entity.getPlan()),
                SubscriptionStatus.valueOf(entity.getStatus()),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getNextBillingDate(),
                new Money(entity.getPrice()),
                BillingPeriod.valueOf(entity.getBillingPeriod()),
                entity.getUserLimit(),
                entity.getStudentLimit(),
                entity.getStorageLimit(),
                entity.getTrialEndDate(),
                entity.isAutoRenew(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}