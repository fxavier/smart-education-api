package com.xavier.smarteducationapi.tenant.domain.event;

import com.xavier.smarteducationapi.common.domain.event.AbstractDomainEvent;
import java.time.Instant;

/**
 * Event raised when a tenant is deactivated.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class TenantDeactivatedEvent extends AbstractDomainEvent {

    private final String tenantName;
    private final Instant deactivatedAt;

    public TenantDeactivatedEvent(String tenantId, String tenantName, Instant deactivatedAt) {
        super(tenantId, "Tenant");
        this.tenantName = tenantName;
        this.deactivatedAt = deactivatedAt;
    }

    @Override
    public String topic() {
        return "tenant.deactivated";
    }

    public String getTenantName() {
        return tenantName;
    }

    public Instant getDeactivatedAt() {
        return deactivatedAt;
    }
}