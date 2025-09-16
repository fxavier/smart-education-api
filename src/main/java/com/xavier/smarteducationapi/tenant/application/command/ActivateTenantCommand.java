package com.xavier.smarteducationapi.tenant.application.command;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * Command to activate a tenant.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Data
@Builder
public class ActivateTenantCommand {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}