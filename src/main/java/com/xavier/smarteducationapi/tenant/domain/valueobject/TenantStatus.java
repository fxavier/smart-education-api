package com.xavier.smarteducationapi.tenant.domain.valueobject;


/**
 * Tenant status enumeration.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public enum TenantStatus {
    PENDING,     // Newly created, awaiting activation
    ACTIVE,      // Active and operational
    SUSPENDED,   // Temporarily suspended (e.g., payment issues)
    INACTIVE,    // Deactivated but data retained
    DELETED      // Marked for deletion
}