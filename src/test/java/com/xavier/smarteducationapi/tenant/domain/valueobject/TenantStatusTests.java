package com.xavier.smarteducationapi.tenant.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the TenantStatus enumeration.
 * 
 * @author Xavier Nhagumbe
 */
@DisplayName("TenantStatus Tests")
class TenantStatusTests {

    @Test
    @DisplayName("Should have all expected status values")
    void shouldHaveAllExpectedStatusValues() {
        TenantStatus[] statuses = TenantStatus.values();
        
        assertEquals(5, statuses.length);
        
        // Check that all expected statuses exist
        boolean hasPending = false;
        boolean hasActive = false;
        boolean hasSuspended = false;
        boolean hasInactive = false;
        boolean hasDeleted = false;
        
        for (TenantStatus status : statuses) {
            switch (status) {
                case PENDING -> hasPending = true;
                case ACTIVE -> hasActive = true;
                case SUSPENDED -> hasSuspended = true;
                case INACTIVE -> hasInactive = true;
                case DELETED -> hasDeleted = true;
            }
        }
        
        assertTrue(hasPending, "Should have PENDING status");
        assertTrue(hasActive, "Should have ACTIVE status");
        assertTrue(hasSuspended, "Should have SUSPENDED status");
        assertTrue(hasInactive, "Should have INACTIVE status");
        assertTrue(hasDeleted, "Should have DELETED status");
    }

    @Test
    @DisplayName("Should be able to convert to string")
    void shouldBeAbleToConvertToString() {
        assertEquals("PENDING", TenantStatus.PENDING.toString());
        assertEquals("ACTIVE", TenantStatus.ACTIVE.toString());
        assertEquals("SUSPENDED", TenantStatus.SUSPENDED.toString());
        assertEquals("INACTIVE", TenantStatus.INACTIVE.toString());
        assertEquals("DELETED", TenantStatus.DELETED.toString());
    }

    @Test
    @DisplayName("Should be able to convert from string")
    void shouldBeAbleToConvertFromString() {
        assertEquals(TenantStatus.PENDING, TenantStatus.valueOf("PENDING"));
        assertEquals(TenantStatus.ACTIVE, TenantStatus.valueOf("ACTIVE"));
        assertEquals(TenantStatus.SUSPENDED, TenantStatus.valueOf("SUSPENDED"));
        assertEquals(TenantStatus.INACTIVE, TenantStatus.valueOf("INACTIVE"));
        assertEquals(TenantStatus.DELETED, TenantStatus.valueOf("DELETED"));
    }

    @Test
    @DisplayName("Should have consistent name() method")
    void shouldHaveConsistentNameMethod() {
        assertEquals("PENDING", TenantStatus.PENDING.name());
        assertEquals("ACTIVE", TenantStatus.ACTIVE.name());
        assertEquals("SUSPENDED", TenantStatus.SUSPENDED.name());
        assertEquals("INACTIVE", TenantStatus.INACTIVE.name());
        assertEquals("DELETED", TenantStatus.DELETED.name());
    }

    @Test
    @DisplayName("Should have ordinal values")
    void shouldHaveOrdinalValues() {
        // Just verify they're not null and are sequential
        assertNotNull(TenantStatus.PENDING.ordinal());
        assertNotNull(TenantStatus.ACTIVE.ordinal());
        assertNotNull(TenantStatus.SUSPENDED.ordinal());
        assertNotNull(TenantStatus.INACTIVE.ordinal());
        assertNotNull(TenantStatus.DELETED.ordinal());
        
        // Verify they're sequential (0, 1, 2, 3, 4)
        assertEquals(0, TenantStatus.PENDING.ordinal());
        assertEquals(1, TenantStatus.ACTIVE.ordinal());
        assertEquals(2, TenantStatus.SUSPENDED.ordinal());
        assertEquals(3, TenantStatus.INACTIVE.ordinal());
        assertEquals(4, TenantStatus.DELETED.ordinal());
    }
}
