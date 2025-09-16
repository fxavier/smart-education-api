package com.xavier.smarteducationapi.tenant.infrastructure.persistence.mapper;

import com.xavier.smarteducationapi.common.domain.valueobject.*;
import com.xavier.smarteducationapi.tenant.domain.entity.Tenant;
import com.xavier.smarteducationapi.tenant.domain.valueobject.TenantStatus;
import com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity.TenantJpaEntity;
import com.xavier.smarteducationapi.tenant.infrastructure.persistence.entity.AddressEmbeddable;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Mapper for converting between Tenant domain and JPA entities.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Component
public class TenantMapper {

    public TenantJpaEntity toJpaEntity(Tenant tenant) {
        if (tenant == null) {
            return null;
        }

        // Build the address embeddable
        AddressEmbeddable addressEmbeddable = null;
        if (tenant.getAddress() != null) {
            addressEmbeddable = AddressEmbeddable.builder()
                    .street(tenant.getAddress().getStreet())
                    .neighborhood(tenant.getAddress().getNeighborhood())
                    .city(tenant.getAddress().getCity())
                    .province(tenant.getAddress().getProvince())
                    .postalCode(tenant.getAddress().getPostalCode())
                    .country(tenant.getAddress().getCountry())
                    .build();
        }

        // Build the JPA entity
        return TenantJpaEntity.builder()
                .id(tenant.getId().toString())
                .name(tenant.getName())
                .subdomain(tenant.getSubdomain())
                .status(tenant.getStatus().name())
                .primaryEmail(tenant.getPrimaryEmail().getValue())
                .primaryPhone(tenant.getPrimaryPhone().getValue())
                .address(addressEmbeddable)
                .taxId(tenant.getTaxId())
                .registrationNumber(tenant.getRegistrationNumber())
                .features(tenant.getFeatures() != null ? new HashSet<>(tenant.getFeatures()) : new HashSet<>())
                .maxUsers(tenant.getMaxUsers())
                .maxStudents(tenant.getMaxStudents())
                .createdAt(tenant.getCreatedAt())
                .activatedAt(tenant.getActivatedAt())
                .suspendedAt(tenant.getSuspendedAt())
                .suspensionReason(tenant.getSuspensionReason())
                .version(tenant.getVersion())
                .build();
    }

    public Tenant toDomainEntity(TenantJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        // Create value objects
        Email email = new Email(entity.getPrimaryEmail());
        Phone phone = new Phone(entity.getPrimaryPhone());

        Address address = null;
        if (entity.getAddress() != null) {
            AddressEmbeddable addr = entity.getAddress();
            address = Address.builder()
                    .street(addr.getStreet())
                    .neighborhood(addr.getNeighborhood())
                    .city(addr.getCity())
                    .province(addr.getProvince())
                    .postalCode(addr.getPostalCode())
                    .country(addr.getCountry())
                    .build();
        }

        // Use the reconstruct method
        return Tenant.reconstruct(
                TenantId.of(entity.getId()),
                entity.getName(),
                entity.getSubdomain(),
                TenantStatus.valueOf(entity.getStatus()),
                email,
                phone,
                address,
                entity.getTaxId(),
                entity.getRegistrationNumber(),
                entity.getFeatures() != null ? new HashSet<>(entity.getFeatures()) : new HashSet<>(),
                entity.getMaxUsers(),
                entity.getMaxStudents(),
                entity.getCreatedAt(),
                entity.getActivatedAt(),
                entity.getSuspendedAt(),
                entity.getSuspensionReason(),
                entity.getVersion()
        );
    }
}