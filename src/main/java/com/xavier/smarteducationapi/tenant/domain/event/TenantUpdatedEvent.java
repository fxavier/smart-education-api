package com.xavier.smarteducationapi.tenant.domain.event;

import com.xavier.smarteducationapi.common.domain.event.AbstractDomainEvent;
import java.time.Instant;

/**
 * Event raised when tenant information is updated.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class TenantUpdatedEvent extends AbstractDomainEvent {

    private final String tenantName;
    private final String updateType;
    private final Instant updatedAt;

    public TenantUpdatedEvent(String tenantId, String tenantName, String updateType) {
        super(tenantId, "Tenant");
        this.tenantName = tenantName;
        this.updateType = updateType;
        this.updatedAt = Instant.now();
    }

    @Override
    public String topic() {
        return "tenant.updated";
    }

    public String getTenantName() {
        return tenantName;
    }

    public String getUpdateType() {
        return updateType;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}