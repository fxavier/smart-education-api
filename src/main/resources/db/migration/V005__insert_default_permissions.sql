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
                                                                     ('TENANT', 'SUSPEND', 'Suspend tenants')
ON CONFLICT (resource, action) DO NOTHING;

-- User permissions
INSERT INTO security.permissions (resource, action, description) VALUES
                                                                     ('USER', 'CREATE', 'Create new users'),
                                                                     ('USER', 'READ', 'View user information'),
                                                                     ('USER', 'UPDATE', 'Update user information'),
                                                                     ('USER', 'DELETE', 'Delete users')
ON CONFLICT (resource, action) DO NOTHING;

-- Role permissions
INSERT INTO security.permissions (resource, action, description) VALUES
                                                                     ('ROLE', 'CREATE', 'Create new roles'),
                                                                     ('ROLE', 'READ', 'View role information'),
                                                                     ('ROLE', 'UPDATE', 'Update role information'),
                                                                     ('ROLE', 'DELETE', 'Delete roles')
ON CONFLICT (resource, action) DO NOTHING;

-- Student permissions
INSERT INTO security.permissions (resource, action, description) VALUES
                                                                     ('STUDENT', 'CREATE', 'Create new students'),
                                                                     ('STUDENT', 'READ', 'View student information'),
                                                                     ('STUDENT', 'UPDATE', 'Update student information'),
                                                                     ('STUDENT', 'DELETE', 'Delete students')
ON CONFLICT (resource, action) DO NOTHING;

-- Teacher permissions
INSERT INTO security.permissions (resource, action, description) VALUES
                                                                     ('TEACHER', 'CREATE', 'Create new teachers'),
                                                                     ('TEACHER', 'READ', 'View teacher information'),
                                                                     ('TEACHER', 'UPDATE', 'Update teacher information'),
                                                                     ('TEACHER', 'DELETE', 'Delete teachers')
ON CONFLICT (resource, action) DO NOTHING;