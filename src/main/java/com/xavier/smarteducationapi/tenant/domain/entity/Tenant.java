package com.xavier.smarteducationapi.tenant.domain.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.xavier.smarteducationapi.common.domain.entity.AggregateRoot;
import com.xavier.smarteducationapi.common.domain.exception.BusinessRuleViolationException;
import com.xavier.smarteducationapi.common.domain.valueobject.Address;
import com.xavier.smarteducationapi.common.domain.valueobject.Email;
import com.xavier.smarteducationapi.common.domain.valueobject.Phone;
import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;
import com.xavier.smarteducationapi.tenant.domain.event.TenantActivatedEvent;
import com.xavier.smarteducationapi.tenant.domain.event.TenantCreatedEvent;
import com.xavier.smarteducationapi.tenant.domain.event.TenantFeatureDisabledEvent;
import com.xavier.smarteducationapi.tenant.domain.event.TenantFeatureEnabledEvent;
import com.xavier.smarteducationapi.tenant.domain.event.TenantLimitsUpdatedEvent;
import com.xavier.smarteducationapi.tenant.domain.event.TenantSuspendedEvent;
import com.xavier.smarteducationapi.tenant.domain.event.TenantUpdatedEvent;
import com.xavier.smarteducationapi.tenant.domain.valueobject.TenantStatus;

/**
 * Tenant aggregate root representing an organization using the system.
 * Manages tenant lifecycle, status, and contact information.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class Tenant extends AggregateRoot<TenantId> {

    private String name;
    private String subdomain;
    private TenantStatus status;
    private Email primaryEmail;
    private Phone primaryPhone;
    private Address address;
    private String taxId;
    private String registrationNumber;
    private Instant createdAt;
    private Instant activatedAt;
    private Instant suspendedAt;
    private String suspensionReason;
    private Set<String> features;
    private Integer maxUsers;
    private Integer maxStudents;

    // Private constructor for builder
    private Tenant() {
        this.features = new HashSet<>();
    }

    /**
     * Protected method for reconstruction from persistence.
     * Used by the infrastructure layer to recreate domain objects.
     */
    public static Tenant reconstruct(
            TenantId id,
            String name,
            String subdomain,
            TenantStatus status,
            Email primaryEmail,
            Phone primaryPhone,
            Address address,
            String taxId,
            String registrationNumber,
            Set<String> features,
            Integer maxUsers,
            Integer maxStudents,
            Instant createdAt,
            Instant activatedAt,
            Instant suspendedAt,
            String suspensionReason,
            Long version) {

        Tenant tenant = new Tenant();
        tenant.setId(id);
        tenant.name = name;
        tenant.subdomain = subdomain;
        tenant.status = status;
        tenant.primaryEmail = primaryEmail;
        tenant.primaryPhone = primaryPhone;
        tenant.address = address;
        tenant.taxId = taxId;
        tenant.registrationNumber = registrationNumber;
        tenant.features = features != null ? new HashSet<>(features) : new HashSet<>();
        tenant.maxUsers = maxUsers;
        tenant.maxStudents = maxStudents;
        tenant.createdAt = createdAt;
        tenant.activatedAt = activatedAt;
        tenant.suspendedAt = suspendedAt;
        tenant.suspensionReason = suspensionReason;
        tenant.setVersion(version);

        return tenant;
    }
    // Factory method
    public static Tenant create(
            TenantId tenantId,
            String name,
            String subdomain,
            Email primaryEmail,
            Phone primaryPhone,
            Address address) {

        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        tenant.name = name;
        tenant.subdomain = validateSubdomain(subdomain);
        tenant.status = TenantStatus.PENDING;
        tenant.primaryEmail = primaryEmail;
        tenant.primaryPhone = primaryPhone;
        tenant.address = address;
        tenant.createdAt = Instant.now();
        tenant.features = new HashSet<>();
        tenant.maxUsers = 10; // Default limit
        tenant.maxStudents = 100; // Default limit

        tenant.registerEvent(new TenantCreatedEvent(
                tenantId.toString(),
                name,
                subdomain,
                primaryEmail.getValue()
        ));

        return tenant;
    }

    // Business methods
    public void activate() {
        if (status != TenantStatus.PENDING) {
            throw new BusinessRuleViolationException(
                    "TenantActivation",
                    "Tenant can only be activated from PENDING status",
                    status
            );
        }

        this.status = TenantStatus.ACTIVE;
        this.activatedAt = Instant.now();
        incrementVersion();

        registerEvent(new TenantActivatedEvent(
                getId().toString(),
                name,
                activatedAt
        ));
    }

    public void suspend(String reason) {
        if (status != TenantStatus.ACTIVE) {
            throw new BusinessRuleViolationException(
                    "TenantSuspension",
                    "Only active tenants can be suspended",
                    status
            );
        }

        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Suspension reason is required");
        }

        this.status = TenantStatus.SUSPENDED;
        this.suspendedAt = Instant.now();
        this.suspensionReason = reason;
        incrementVersion();

        registerEvent(new TenantSuspendedEvent(
                getId().toString(),
                name,
                reason,
                suspendedAt
        ));
    }

    public void reactivate() {
        if (status != TenantStatus.SUSPENDED) {
            throw new BusinessRuleViolationException(
                    "TenantReactivation",
                    "Only suspended tenants can be reactivated",
                    status
            );
        }

        this.status = TenantStatus.ACTIVE;
        this.suspendedAt = null;
        this.suspensionReason = null;
        incrementVersion();

        registerEvent(new TenantActivatedEvent(
                getId().toString(),
                name,
                Instant.now()
        ));
    }


    public boolean hasFeature(String featureCode) {
        return features.contains(featureCode);
    }

    private static String validateSubdomain(String subdomain) {
        if (subdomain == null || subdomain.trim().isEmpty()) {
            throw new IllegalArgumentException("Subdomain is required");
        }

        String normalized = subdomain.toLowerCase().trim();

        if (!normalized.matches("^[a-z0-9-]{3,63}$")) {
            throw new IllegalArgumentException(
                    "Subdomain must be 3-63 characters and contain only lowercase letters, numbers, and hyphens"
            );
        }

        if (normalized.startsWith("-") || normalized.endsWith("-")) {
            throw new IllegalArgumentException(
                    "Subdomain cannot start or end with a hyphen"
            );
        }

        return normalized;
    }

    // Getters
    public String getName() { return name; }
    public String getSubdomain() { return subdomain; }
    public TenantStatus getStatus() { return status; }
    public Email getPrimaryEmail() { return primaryEmail; }
    public Phone getPrimaryPhone() { return primaryPhone; }
    public Address getAddress() { return address; }
    public String getTaxId() { return taxId; }
    public String getRegistrationNumber() { return registrationNumber; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getActivatedAt() { return activatedAt; }
    public Instant getSuspendedAt() { return suspendedAt; }
    public String getSuspensionReason() { return suspensionReason; }
    public Set<String> getFeatures() { return new HashSet<>(features); }
    public Integer getMaxUsers() { return maxUsers; }
    public Integer getMaxStudents() { return maxStudents; }

    // Setters for business registration
    public void setBusinessRegistration(String taxId, String registrationNumber) {
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
        incrementVersion();
    }

    public void updateContactInfo(Email email, Phone phone, Address address) {
        this.primaryEmail = email;
        this.primaryPhone = phone;
        this.address = address;
        incrementVersion();

        // Register event
        registerEvent(new TenantUpdatedEvent(
                getId().toString(),
                name,
                "CONTACT_INFO_UPDATED"
        ));
    }

    public void enableFeature(String featureCode) {
        features.add(featureCode);
        incrementVersion();

        // Register event
        registerEvent(new TenantFeatureEnabledEvent(
                getId().toString(),
                name,
                featureCode
        ));
    }

    public void disableFeature(String featureCode) {
        features.remove(featureCode);
        incrementVersion();

        // Register event
        registerEvent(new TenantFeatureDisabledEvent(
                getId().toString(),
                name,
                featureCode
        ));
    }

    public void updateLimits(Integer maxUsers, Integer maxStudents) {
        if (maxUsers != null && maxUsers < 1) {
            throw new IllegalArgumentException("Max users must be at least 1");
        }
        if (maxStudents != null && maxStudents < 1) {
            throw new IllegalArgumentException("Max students must be at least 1");
        }

        Integer previousMaxUsers = this.maxUsers;
        Integer previousMaxStudents = this.maxStudents;

        this.maxUsers = maxUsers;
        this.maxStudents = maxStudents;
        incrementVersion();

        // Register event
        registerEvent(new TenantLimitsUpdatedEvent(
                getId().toString(),
                name,
                maxUsers,
                maxStudents,
                previousMaxUsers,
                previousMaxStudents
        ));
    }
}