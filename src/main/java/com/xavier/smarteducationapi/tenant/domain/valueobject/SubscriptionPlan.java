package com.xavier.smarteducationapi.tenant.domain.valueobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Subscription plan enumeration.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Getter
@RequiredArgsConstructor
public enum SubscriptionPlan {
    BASIC("Basic", "Essential features for small schools"),
    STANDARD("Standard", "Advanced features for growing institutions"),
    PROFESSIONAL("Professional", "Complete solution for established schools"),
    ENTERPRISE("Enterprise", "Unlimited access with custom features"),

    CUSTOM( "Custom", "Tailored solutions for large organizations");

    private final String displayName;
    private final String description;


}