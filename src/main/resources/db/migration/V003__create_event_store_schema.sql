-- ========= src/main/resources/db/migration/V003__create_event_store_schema.sql =========
-- Event store for domain events and event sourcing
-- Author: Xavier Nhagumbe
-- Date: 2025-09-15

CREATE SCHEMA IF NOT EXISTS event_store;

-- Domain events table
CREATE TABLE IF NOT EXISTS event_store.domain_events (
                                                         event_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    aggregate_id VARCHAR(255) NOT NULL,
    aggregate_type VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    event_version INTEGER DEFAULT 1,
    event_data JSONB NOT NULL,
    metadata JSONB,
    occurred_on TIMESTAMP NOT NULL,
    published BOOLEAN DEFAULT FALSE,
    published_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Create indexes for event store
CREATE INDEX IF NOT EXISTS idx_domain_events_aggregate ON event_store.domain_events(aggregate_id, aggregate_type);
CREATE INDEX IF NOT EXISTS idx_domain_events_occurred ON event_store.domain_events(occurred_on DESC);
CREATE INDEX IF NOT EXISTS idx_domain_events_published ON event_store.domain_events(published);
CREATE INDEX IF NOT EXISTS idx_domain_events_type ON event_store.domain_events(event_type);

-- Integration events table
CREATE TABLE IF NOT EXISTS event_store.integration_events (
                                                              event_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    event_type VARCHAR(255) NOT NULL,
    source_context VARCHAR(100) NOT NULL,
    target_contexts TEXT[],
    payload JSONB NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    attempts INTEGER DEFAULT 0,
    max_attempts INTEGER DEFAULT 3,
    last_attempt_at TIMESTAMP,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP,

    CONSTRAINT chk_integration_status CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'DEAD_LETTER'))
    );

CREATE INDEX IF NOT EXISTS idx_integration_events_status ON event_store.integration_events(status);
CREATE INDEX IF NOT EXISTS idx_integration_events_created ON event_store.integration_events(created_at);