-- ========= src/main/resources/db/migration/V006__insert_system_roles.sql =========
-- Insert system roles
-- Author: Xavier Nhagumbe
-- Date: 2025-09-15

-- Insert system roles (tenant_id NULL means system-wide)
INSERT INTO security.roles (name, description, is_system, created_by) VALUES
                                                                          ('SUPER_ADMIN', 'System super administrator with full access', true, 'SYSTEM'),
                                                                          ('SYSTEM_ADMIN', 'System administrator', true, 'SYSTEM');

-- Assign all permissions to SUPER_ADMIN role
INSERT INTO security.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM security.roles r
         CROSS JOIN security.permissions p
WHERE r.name = 'SUPER_ADMIN';

-- ========= src/main/resources/db/migration/V007__create_audit_schema.sql =========
-- Audit tables for tracking changes
-- Author: Xavier Nhagumbe
-- Date: 2025-09-15

CREATE SCHEMA IF NOT EXISTS audit;

-- Audit log table
CREATE TABLE IF NOT EXISTS audit.audit_log (
                                               id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID,
    user_id UUID,
    entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL,
    old_values JSONB,
    new_values JSONB,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Create indexes
CREATE INDEX idx_audit_log_tenant ON audit.audit_log(tenant_id);
CREATE INDEX idx_audit_log_user ON audit.audit_log(user_id);
CREATE INDEX idx_audit_log_entity ON audit.audit_log(entity_type, entity_id);
CREATE INDEX idx_audit_log_created ON audit.audit_log(created_at DESC);