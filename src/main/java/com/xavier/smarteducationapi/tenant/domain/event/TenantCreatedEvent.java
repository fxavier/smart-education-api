package com.xavier.smarteducationapi.tenant.domain.event;

import com.xavier.smarteducationapi.common.domain.event.AbstractDomainEvent;

/**
 * Event raised when a new tenant is created.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class TenantCreatedEvent extends AbstractDomainEvent {

    private final String tenantName;
    private final String subdomain;
    private final String primaryEmail;

    public TenantCreatedEvent(String tenantId, String tenantName, String subdomain, String primaryEmail) {
        super(tenantId, "Tenant");
        this.tenantName = tenantName;
        this.subdomain = subdomain;
        this.primaryEmail = primaryEmail;
    }

    @Override
    public String topic() {
        return "tenant.created";
    }

    public String getTenantName() { return tenantName; }
    public String getSubdomain() { return subdomain; }
    public String getPrimaryEmail() { return primaryEmail; }
}