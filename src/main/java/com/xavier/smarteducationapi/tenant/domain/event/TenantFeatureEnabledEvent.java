package com.xavier.smarteducationapi.tenant.domain.event;

import com.xavier.smarteducationapi.common.domain.event.AbstractDomainEvent;
import java.time.Instant;

/**
 * Event raised when a feature is enabled for a tenant.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class TenantFeatureEnabledEvent extends AbstractDomainEvent {

    private final String tenantName;
    private final String featureCode;
    private final Instant enabledAt;

    public TenantFeatureEnabledEvent(String tenantId, String tenantName, String featureCode) {
        super(tenantId, "Tenant");
        this.tenantName = tenantName;
        this.featureCode = featureCode;
        this.enabledAt = Instant.now();
    }

    @Override
    public String topic() {
        return "tenant.feature.enabled";
    }

    public String getTenantName() {
        return tenantName;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public Instant getEnabledAt() {
        return enabledAt;
    }
}