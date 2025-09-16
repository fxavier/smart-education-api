-- ========= src/main/resources/db/migration/V004__create_security_schema.sql =========
-- Security and authentication tables
-- Author: Xavier Nhagumbe
-- Date: 2025-09-15

CREATE SCHEMA IF NOT EXISTS security;

-- Users table (system-wide)
CREATE TABLE IF NOT EXISTS security.users (
                                              id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID REFERENCES tenants(id),
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(50),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    email_verified BOOLEAN DEFAULT FALSE,
    phone_verified BOOLEAN DEFAULT FALSE,
    last_login_at TIMESTAMP,
    failed_login_attempts INTEGER DEFAULT 0,
    locked_until TIMESTAMP,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100),
    version BIGINT DEFAULT 0,

    CONSTRAINT chk_user_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'LOCKED')),
    CONSTRAINT uq_user_email_tenant UNIQUE (email, tenant_id)
    );

-- Roles table
CREATE TABLE IF NOT EXISTS security.roles (
                                              id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID REFERENCES tenants(id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_system BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),

    CONSTRAINT uq_role_name_tenant UNIQUE (name, tenant_id)
    );

-- Permissions table
CREATE TABLE IF NOT EXISTS security.permissions (
                                                    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    resource VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL,
    description TEXT,

    CONSTRAINT uq_permission UNIQUE (resource, action)
    );

-- User-Role relationship
CREATE TABLE IF NOT EXISTS security.user_roles (
                                                   user_id UUID REFERENCES security.users(id) ON DELETE CASCADE,
    role_id UUID REFERENCES security.roles(id) ON DELETE CASCADE,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(100),

    PRIMARY KEY (user_id, role_id)
    );

-- Role-Permission relationship
CREATE TABLE IF NOT EXISTS security.role_permissions (
                                                         role_id UUID REFERENCES security.roles(id) ON DELETE CASCADE,
    permission_id UUID REFERENCES security.permissions(id) ON DELETE CASCADE,

    PRIMARY KEY (role_id, permission_id)
    );

-- Refresh tokens
CREATE TABLE IF NOT EXISTS security.refresh_tokens (
                                                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES security.users(id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    revoked_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent TEXT
    );

-- Create indexes
CREATE INDEX idx_users_tenant ON security.users(tenant_id);
CREATE INDEX idx_users_email ON security.users(email);
CREATE INDEX idx_roles_tenant ON security.roles(tenant_id);
CREATE INDEX idx_refresh_tokens_user ON security.refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_expires ON security.refresh_tokens(expires_at);

-- ========= src/main/resources/db/migration/V005__insert_default_permissions.sql =========
-- Insert default system permissions
-- Author: Xavier Nhagumbe
-- Date: 2025-09-15

-- Tenant permissions
INSERT INTO security.permissions (resource, action, description) VALUES
                                                                     ('TENANT', 'CREATE', 'Create new tenants'),
                                                                     ('TENANT', 'READ', 'View tenant information'),
                                                                     ('TENANT', 'UPDATE', 'Update tenant information'),
                                                                     ('TENANT', 'DELETE', 'Delete tenants'),
                                                                     ('TENANT', 'ACTIVATE', 'Activate tenants'),
                                                                     ('TENANT', 'SUSPEND', 'Suspend tenants');

-- User permissions
INSERT INTO security.permissions (resource, action, description) VALUES
                                                                     ('USER', 'CREATE', 'Create new users'),
                                                                     ('USER', 'READ', 'View user information'),
                                                                     ('USER', 'UPDATE', 'Update user information'),
                                                                     ('USER', 'DELETE', 'Delete users');

-- Role permissions
INSERT INTO security.permissions (resource, action, description) VALUES
                                                                     ('ROLE', 'CREATE', 'Create new roles'),
                                                                     ('ROLE', 'READ', 'View role information'),
                                                                     ('ROLE', 'UPDATE', 'Update role information'),
                                                                     ('ROLE', 'DELETE', 'Delete roles');

-- Student permissions
INSERT INTO security.permissions (resource, action, description) VALUES
                                                                     ('STUDENT', 'CREATE', 'Create new students'),
                                                                     ('STUDENT', 'READ', 'View student information'),
                                                                     ('STUDENT', 'UPDATE', 'Update student information'),
                                                                     ('STUDENT', 'DELETE', 'Delete students');

-- Teacher permissions
INSERT INTO security.permissions (resource, action, description) VALUES
                                                                     ('TEACHER', 'CREATE', 'Create new teachers'),
                                                                     ('TEACHER', 'READ', 'View teacher information'),
                                                                     ('TEACHER', 'UPDATE', 'Update teacher information'),
                                                                     ('TEACHER', 'DELETE', 'Delete teachers');