package com.xavier.smarteducationapi.tenant.application.command;

import com.xavier.smarteducationapi.tenant.domain.valueobject.SubscriptionPlan;
import com.xavier.smarteducationapi.tenant.domain.valueobject.BillingPeriod;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Command to create a new subscription.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Data
@Builder
public class CreateSubscriptionCommand {

    @NotNull(message = "Tenant ID is required")
    private String tenantId;

    @NotNull(message = "Subscription plan is required")
    private SubscriptionPlan plan;

    @NotNull(message = "Billing period is required")
    private BillingPeriod billingPeriod;

    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @Builder.Default
    private boolean autoRenew = true;
}