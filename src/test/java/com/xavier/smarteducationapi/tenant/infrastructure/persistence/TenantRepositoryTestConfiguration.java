package com.xavier.smarteducationapi.tenant.infrastructure.persistence;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Minimal test configuration for repository integration tests.
 * Only loads the essential components needed for JPA testing.
 * 
 * @author Xavier Nhagumbe
 */
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {
    "com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity",
    "com.xavier.smarteducationapi.common.domain.valueobject"
})
@EnableJpaRepositories(basePackages = {
    "com.xavier.smarteducationapi.tenant.infrastructure.persistence.repository"
})
@ComponentScan(basePackages = {
    "com.xavier.smarteducationapi.tenant.infrastructure.persistence"
})
public class TenantRepositoryTestConfiguration {
    // Empty - just configuration annotations
}
