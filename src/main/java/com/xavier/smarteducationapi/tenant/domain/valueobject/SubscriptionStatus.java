package com.xavier.smarteducationapi.tenant.domain.valueobject;


/**
 * Subscription status enumeration.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public enum SubscriptionStatus {
    TRIAL,       // In trial period
    ACTIVE,      // Active subscription
    SUSPENDED,   // Suspended (payment issues)
    EXPIRED,     // Expired subscription
    CANCELLED    // Cancelled by user
}