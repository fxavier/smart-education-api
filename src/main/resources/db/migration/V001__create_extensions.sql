-- ========= src/main/resources/db/migration/V001__create_extensions.sql =========
-- Create PostgreSQL extensions
-- Author: Xavier Nhagumbe
-- Date: 2025-09-15

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";