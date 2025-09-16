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