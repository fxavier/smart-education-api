package com.xavier.smarteducationapi.tenant.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuration for the Tenant module.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Configuration
@ComponentScan(basePackages = {
        "com.xavier.smarteducationapi.tenant.domain",
        "com.xavier.smarteducationapi.tenant.application",
        "com.xavier.smarteducationapi.tenant.infrastructure"
})
@EntityScan(basePackages = {
        "com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity"
})
@EnableJpaRepositories(basePackages = {
        "com.xavier.smarteducationapi.tenant.infrastructure.persistence.repository"
})
public class TenantModuleConfig {
    // Additional bean configurations if needed
}