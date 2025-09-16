package com.xavier.smarteducationapi.tenant.domain.entity;

import com.xavier.smarteducationapi.common.domain.entity.BaseEntity;
import com.xavier.smarteducationapi.common.domain.valueobject.*;
import com.xavier.smarteducationapi.tenant.domain.event.*;
import com.xavier.smarteducationapi.tenant.domain.valueobject.*;
import com.xavier.smarteducationapi.common.domain.exception.BusinessRuleViolationException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Subscription entity representing a tenant's subscription to a plan.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class Subscription extends BaseEntity<UUID> {

    private TenantId tenantId;
    private SubscriptionPlan plan;
    private SubscriptionStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextBillingDate;
    private Money price;
    private BillingPeriod billingPeriod;
    private Integer userLimit;
    private Integer studentLimit;
    private Integer storageLimit; // in GB
    private LocalDate trialEndDate;
    private boolean autoRenew;
    private Instant createdAt;
    private Instant updatedAt;

    // Factory method
    public static Subscription create(
            TenantId tenantId,
            SubscriptionPlan plan,
            BillingPeriod billingPeriod,
            LocalDate startDate) {

        Subscription subscription = new Subscription();
        subscription.setId(UUID.randomUUID());
        subscription.tenantId = tenantId;
        subscription.plan = plan;
        subscription.billingPeriod = billingPeriod;
        subscription.startDate = startDate;
        subscription.status = SubscriptionStatus.ACTIVE;
        subscription.autoRenew = true;
        subscription.createdAt = Instant.now();
        subscription.updatedAt = Instant.now();

        // Set plan limits based on subscription plan
        subscription.applyPlanLimits(plan);

        // Calculate pricing and dates
        subscription.calculatePricing();
        subscription.calculateEndDate();
        subscription.calculateNextBillingDate();

        // Set trial if applicable
        if (plan == SubscriptionPlan.BASIC) {
            subscription.trialEndDate = startDate.plusDays(14);
        }

        return subscription;
    }

    /**
     * Protected method for reconstruction from persistence.
     * Used by the infrastructure layer to recreate domain objects.
     */
    public static Subscription reconstruct(
            UUID id,
            TenantId tenantId,
            SubscriptionPlan plan,
            SubscriptionStatus status,
            LocalDate startDate,
            LocalDate endDate,
            LocalDate nextBillingDate,
            Money price,
            BillingPeriod billingPeriod,
            Integer userLimit,
            Integer studentLimit,
            Integer storageLimit,
            LocalDate trialEndDate,
            boolean autoRenew,
            Instant createdAt,
            Instant updatedAt) {

        Subscription subscription = new Subscription();
        subscription.setId(id);
        subscription.tenantId = tenantId;
        subscription.plan = plan;
        subscription.status = status;
        subscription.startDate = startDate;
        subscription.endDate = endDate;
        subscription.nextBillingDate = nextBillingDate;
        subscription.price = price;
        subscription.billingPeriod = billingPeriod;
        subscription.userLimit = userLimit;
        subscription.studentLimit = studentLimit;
        subscription.storageLimit = storageLimit;
        subscription.trialEndDate = trialEndDate;
        subscription.autoRenew = autoRenew;
        subscription.createdAt = createdAt;
        subscription.updatedAt = updatedAt;

        return subscription;
    }

    // Business methods
    public void upgrade(SubscriptionPlan newPlan) {
        if (newPlan.ordinal() <= plan.ordinal()) {
            throw new BusinessRuleViolationException(
                    "SubscriptionUpgrade",
                    "Can only upgrade to a higher plan",
                    newPlan
            );
        }

        if (status != SubscriptionStatus.ACTIVE) {
            throw new BusinessRuleViolationException(
                    "SubscriptionUpgrade",
                    "Can only upgrade active subscriptions",
                    status
            );
        }

        SubscriptionPlan oldPlan = this.plan;
        this.plan = newPlan;
        applyPlanLimits(newPlan);
        calculatePricing();
        this.updatedAt = Instant.now();
    }

    public void downgrade(SubscriptionPlan newPlan) {
        if (newPlan.ordinal() >= plan.ordinal()) {
            throw new BusinessRuleViolationException(
                    "SubscriptionDowngrade",
                    "Can only downgrade to a lower plan",
                    newPlan
            );
        }

        if (status != SubscriptionStatus.ACTIVE) {
            throw new BusinessRuleViolationException(
                    "SubscriptionDowngrade",
                    "Can only downgrade active subscriptions",
                    status
            );
        }

        this.plan = newPlan;
        applyPlanLimits(newPlan);
        calculatePricing();
        this.updatedAt = Instant.now();
    }

    public void cancel() {
        if (status == SubscriptionStatus.CANCELLED) {
            throw new BusinessRuleViolationException(
                    "SubscriptionCancellation",
                    "Subscription is already cancelled",
                    status
            );
        }

        this.status = SubscriptionStatus.CANCELLED;
        this.autoRenew = false;
        this.endDate = LocalDate.now();
        this.updatedAt = Instant.now();
    }

    public void suspend() {
        if (status != SubscriptionStatus.ACTIVE) {
            throw new BusinessRuleViolationException(
                    "SubscriptionSuspension",
                    "Can only suspend active subscriptions",
                    status
            );
        }

        this.status = SubscriptionStatus.SUSPENDED;
        this.updatedAt = Instant.now();
    }

    public void reactivate() {
        if (status != SubscriptionStatus.SUSPENDED && status != SubscriptionStatus.EXPIRED) {
            throw new BusinessRuleViolationException(
                    "SubscriptionReactivation",
                    "Can only reactivate suspended or expired subscriptions",
                    status
            );
        }

        this.status = SubscriptionStatus.ACTIVE;
        this.startDate = LocalDate.now();
        calculateEndDate();
        calculateNextBillingDate();
        this.updatedAt = Instant.now();
    }

    public void renew() {
        if (status != SubscriptionStatus.ACTIVE && status != SubscriptionStatus.EXPIRED) {
            throw new BusinessRuleViolationException(
                    "SubscriptionRenewal",
                    "Can only renew active or expired subscriptions",
                    status
            );
        }

        this.startDate = LocalDate.now();
        this.status = SubscriptionStatus.ACTIVE;
        calculateEndDate();
        calculateNextBillingDate();
        this.updatedAt = Instant.now();
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(endDate);
    }

    public boolean isInTrial() {
        return trialEndDate != null && LocalDate.now().isBefore(trialEndDate);
    }

    private void applyPlanLimits(SubscriptionPlan plan) {
        switch (plan) {
            case BASIC:
                this.userLimit = 10;
                this.studentLimit = 100;
                this.storageLimit = 5;
                break;
            case STANDARD:
                this.userLimit = 50;
                this.studentLimit = 500;
                this.storageLimit = 25;
                break;
            case PROFESSIONAL:
                this.userLimit = 200;
                this.studentLimit = 2000;
                this.storageLimit = 100;
                break;
            case ENTERPRISE:
                this.userLimit = null; // Unlimited
                this.studentLimit = null; // Unlimited
                this.storageLimit = 500;
                break;
        }
    }

    private void calculatePricing() {
        BigDecimal basePrice;

        switch (plan) {
            case BASIC:
                basePrice = new BigDecimal("29.99");
                break;
            case STANDARD:
                basePrice = new BigDecimal("99.99");
                break;
            case PROFESSIONAL:
                basePrice = new BigDecimal("299.99");
                break;
            case ENTERPRISE:
                basePrice = new BigDecimal("999.99");
                break;
            default:
                basePrice = BigDecimal.ZERO;
        }

        // Apply billing period multiplier
        if (billingPeriod == BillingPeriod.YEARLY) {
            basePrice = basePrice.multiply(new BigDecimal("10")); // 2 months free
        } else if (billingPeriod == BillingPeriod.QUARTERLY) {
            basePrice = basePrice.multiply(new BigDecimal("2.85")); // 5% discount
        }

        this.price = new Money(basePrice);
    }

    private void calculateEndDate() {
        switch (billingPeriod) {
            case MONTHLY:
                this.endDate = startDate.plusMonths(1);
                break;
            case QUARTERLY:
                this.endDate = startDate.plusMonths(3);
                break;
            case YEARLY:
                this.endDate = startDate.plusYears(1);
                break;
        }
    }

    private void calculateNextBillingDate() {
        if (autoRenew) {
            this.nextBillingDate = endDate;
        } else {
            this.nextBillingDate = null;
        }
    }

    // Getters
    public TenantId getTenantId() { return tenantId; }
    public SubscriptionPlan getPlan() { return plan; }
    public SubscriptionStatus getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public LocalDate getNextBillingDate() { return nextBillingDate; }
    public Money getPrice() { return price; }
    public BillingPeriod getBillingPeriod() { return billingPeriod; }
    public Integer getUserLimit() { return userLimit; }
    public Integer getStudentLimit() { return studentLimit; }
    public Integer getStorageLimit() { return storageLimit; }
    public LocalDate getTrialEndDate() { return trialEndDate; }
    public boolean isAutoRenew() { return autoRenew; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setAutoRenew(boolean autoRenew) {
        this.autoRenew = autoRenew;
        calculateNextBillingDate();
        this.updatedAt = Instant.now();
    }
}