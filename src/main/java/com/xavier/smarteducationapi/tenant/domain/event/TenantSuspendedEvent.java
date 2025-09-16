package com.xavier.smarteducationapi.tenant.domain.event;

import com.xavier.smarteducationapi.common.domain.event.AbstractDomainEvent;
import java.time.Instant;

/**
 * Event raised when a tenant is suspended.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class TenantSuspendedEvent extends AbstractDomainEvent {

    private final String tenantName;
    private final String reason;
    private final Instant suspendedAt;

    public TenantSuspendedEvent(String tenantId, String tenantName, String reason, Instant suspendedAt) {
        super(tenantId, "Tenant");
        this.tenantName = tenantName;
        this.reason = reason;
        this.suspendedAt = suspendedAt;
    }

    @Override
    public String topic() {
        return "tenant.suspended";
    }

    public String getTenantName() {
        return tenantName;
    }

    public String getReason() {
        return reason;
    }

    public Instant getSuspendedAt() {
        return suspendedAt;
    }
}