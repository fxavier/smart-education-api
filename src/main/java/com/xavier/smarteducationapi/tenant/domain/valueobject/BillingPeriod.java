package com.xavier.smarteducationapi.tenant.domain.valueobject;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Billing period enumeration.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Getter
@RequiredArgsConstructor
public enum BillingPeriod {
    MONTHLY(1, "Monthly"),
    QUARTERLY(3, "Quarterly"),
    YEARLY(12, "Yearly");

    private final int months;
    private final String displayName;
}