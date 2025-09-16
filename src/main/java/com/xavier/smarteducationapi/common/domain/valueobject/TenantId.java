package com.xavier.smarteducationapi.common.domain.valueobject;

import java.util.UUID;

/**
 * TenantId value object representing a unique tenant identifier.
 * Ensures immutability and uniqueness using UUID.
 * @version 1.0
 * @since 2025-09-15
 * @author Xavier Nhagumbe
 */
public class TenantId extends BaseId<UUID> {

    public TenantId(UUID value) {
        super(value);
    }

    public static TenantId generate() {
        return new TenantId(UUID.randomUUID());
    }

    public static TenantId of(String uuid) {
        return new TenantId(UUID.fromString(uuid));
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}