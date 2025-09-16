package com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA entity for Tenant.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Entity
@Table(name = "tenants", indexes = {
        @Index(name = "idx_tenant_subdomain", columnList = "subdomain", unique = true),
        @Index(name = "idx_tenant_email", columnList = "primary_email", unique = true),
        @Index(name = "idx_tenant_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "subdomain", nullable = false, unique = true, length = 63)
    private String subdomain;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "primary_email", nullable = false, unique = true)
    private String primaryEmail;

    @Column(name = "primary_phone", nullable = false)
    private String primaryPhone;

    @Embedded
    private AddressEmbeddable address;

    @Column(name = "tax_id", length = 50)
    private String taxId;

    @Column(name = "registration_number", length = 50)
    private String registrationNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tenant_features", joinColumns = @JoinColumn(name = "tenant_id"))
    @Column(name = "feature_code")
    private Set<String> features;

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "max_students")
    private Integer maxStudents;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "activated_at")
    private Instant activatedAt;

    @Column(name = "suspended_at")
    private Instant suspendedAt;

    @Column(name = "suspension_reason", length = 500)
    private String suspensionReason;

    @Version
    @Column(name = "version")
    private Long version;
}

