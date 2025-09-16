package com.xavier.smarteducationapi.tenant.application.command;


import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.*;

/**
 * Command to create a new tenant.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Data
@Builder
public class CreateTenantCommand {

    @NotBlank(message = "Tenant name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Subdomain is required")
    @Pattern(regexp = "^[a-z0-9-]{3,63}$",
            message = "Subdomain must be 3-63 characters with only lowercase letters, numbers, and hyphens")
    private String subdomain;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String primaryEmail;

    @NotBlank(message = "Phone is required")
    private String primaryPhone;

    @NotBlank(message = "Street is required")
    private String street;

    private String neighborhood;

    @NotBlank(message = "City is required")
    private String city;

    private String province;

    private String postalCode;

    @NotBlank(message = "Country is required")
    private String country;

    private String taxId;
    private String registrationNumber;
}