package com.xavier.smarteducationapi.tenant.domain.event;

import com.xavier.smarteducationapi.common.domain.event.AbstractDomainEvent;

/**
 * Event raised when tenant limits are updated.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class TenantLimitsUpdatedEvent extends AbstractDomainEvent {

    private final String tenantName;
    private final Integer newMaxUsers;
    private final Integer newMaxStudents;
    private final Integer previousMaxUsers;
    private final Integer previousMaxStudents;

    public TenantLimitsUpdatedEvent(
            String tenantId,
            String tenantName,
            Integer newMaxUsers,
            Integer newMaxStudents,
            Integer previousMaxUsers,
            Integer previousMaxStudents) {
        super(tenantId, "Tenant");
        this.tenantName = tenantName;
        this.newMaxUsers = newMaxUsers;
        this.newMaxStudents = newMaxStudents;
        this.previousMaxUsers = previousMaxUsers;
        this.previousMaxStudents = previousMaxStudents;
    }

    @Override
    public String topic() {
        return "tenant.limits.updated";
    }

    public String getTenantName() {
        return tenantName;
    }

    public Integer getNewMaxUsers() {
        return newMaxUsers;
    }

    public Integer getNewMaxStudents() {
        return newMaxStudents;
    }

    public Integer getPreviousMaxUsers() {
        return previousMaxUsers;
    }

    public Integer getPreviousMaxStudents() {
        return previousMaxStudents;
    }
}