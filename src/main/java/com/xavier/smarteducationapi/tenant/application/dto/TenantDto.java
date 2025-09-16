package com.xavier.smarteducationapi.tenant.application.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.Set;

/**
 * Data Transfer Object for Tenant.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Data
@Builder
public class TenantDto {
    private String id;
    private String name;
    private String subdomain;
    private String status;
    private String primaryEmail;
    private String primaryPhone;
    private String address;
    private String taxId;
    private String registrationNumber;
    private Set<String> features;
    private Integer maxUsers;
    private Integer maxStudents;
    private Instant createdAt;
    private Instant activatedAt;
}