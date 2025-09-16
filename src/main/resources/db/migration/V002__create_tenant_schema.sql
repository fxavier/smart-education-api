-- ========= src/main/resources/db/migration/V002__create_tenant_schema.sql =========
-- Create tenant management tables
-- Author: Xavier Nhagumbe
-- Date: 2025-09-15

-- Tenants table
CREATE TABLE IF NOT EXISTS tenants (
                                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_name VARCHAR(100) NOT NULL,
    subdomain VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL,
    plan VARCHAR(30) NOT NULL,
    contact_email VARCHAR(255) NOT NULL,
    contact_phone VARCHAR(50) NOT NULL,

    -- Address fields
    address_street VARCHAR(255),
    address_neighborhood VARCHAR(100),
    address_city VARCHAR(100) NOT NULL,
    address_province VARCHAR(100),
    address_postal_code VARCHAR(20),
    address_country VARCHAR(100) NOT NULL,

    -- Schema information
    schema_name VARCHAR(63) UNIQUE NOT NULL,

    -- Timestamps
    activated_at TIMESTAMP,
    suspended_at TIMESTAMP,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100),

    -- Optimistic locking
    version BIGINT DEFAULT 0,

    -- Constraints
    CONSTRAINT chk_tenant_status CHECK (status IN ('PENDING_ACTIVATION', 'ACTIVE', 'SUSPENDED', 'INACTIVE', 'TERMINATED')),
    CONSTRAINT chk_subscription_plan CHECK (plan IN ('TRIAL', 'BASIC', 'STANDARD', 'PREMIUM', 'ENTERPRISE'))
    );

-- Create indexes
CREATE INDEX idx_tenants_subdomain ON tenants(subdomain);
CREATE INDEX idx_tenants_status ON tenants(status);
CREATE INDEX idx_tenants_schema_name ON tenants(schema_name);