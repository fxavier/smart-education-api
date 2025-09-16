-- Spring Modulith Event Publication table
CREATE TABLE IF NOT EXISTS event_publication (
                                                 id UUID PRIMARY KEY,
                                                 listener_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    serialized_event TEXT NOT NULL,
    publication_date TIMESTAMP NOT NULL,
    completion_date TIMESTAMP,
    CONSTRAINT uk_event_publication UNIQUE (listener_id, event_type, serialized_event)
    );

-- Index for incomplete events
CREATE INDEX idx_event_publication_incomplete
    ON event_publication(listener_id, completion_date)
    WHERE completion_date IS NULL;