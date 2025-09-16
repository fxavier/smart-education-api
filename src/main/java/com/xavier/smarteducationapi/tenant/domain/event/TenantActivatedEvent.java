package com.xavier.smarteducationapi.tenant.domain.event;

import com.xavier.smarteducationapi.common.domain.event.AbstractDomainEvent;
import java.time.Instant;

/**
 * Event raised when a tenant is activated.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class TenantActivatedEvent extends AbstractDomainEvent {

    private final String tenantName;
    private final Instant activatedAt;

    public TenantActivatedEvent(String tenantId, String tenantName, Instant activatedAt) {
        super(tenantId, "Tenant");
        this.tenantName = tenantName;
        this.activatedAt = activatedAt;
    }

    @Override
    public String topic() {
        return "tenant.activated";
    }

    public String getTenantName() { return tenantName; }
    public Instant getActivatedAt() { return activatedAt; }
}