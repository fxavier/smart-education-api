package com.xavier.smarteducationapi.tenant.domain.event;

import com.xavier.smarteducationapi.common.domain.event.AbstractDomainEvent;
import java.time.Instant;

/**
 * Event raised when a tenant is deleted or marked for deletion.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class TenantDeletedEvent extends AbstractDomainEvent {

    private final String tenantName;
    private final String subdomain;
    private final Instant deletedAt;

    public TenantDeletedEvent(String tenantId, String tenantName, String subdomain) {
        super(tenantId, "Tenant");
        this.tenantName = tenantName;
        this.subdomain = subdomain;
        this.deletedAt = Instant.now();
    }

    @Override
    public String topic() {
        return "tenant.deleted";
    }

    public String getTenantName() {
        return tenantName;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}