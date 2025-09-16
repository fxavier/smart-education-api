package com.xavier.smarteducationapi.tenant.application.service;

import com.xavier.smarteducationapi.common.domain.valueobject.*;
import com.xavier.smarteducationapi.tenant.domain.entity.Tenant;
import com.xavier.smarteducationapi.tenant.domain.repository.TenantRepository;
import com.xavier.smarteducationapi.tenant.domain.service.TenantDomainService;
import com.xavier.smarteducationapi.tenant.application.command.*;
import com.xavier.smarteducationapi.tenant.application.dto.TenantDto;
import com.xavier.smarteducationapi.common.domain.exception.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application service for tenant management.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TenantApplicationService {

    private final TenantRepository tenantRepository;
    private final TenantDomainService tenantDomainService;

    public TenantDto createTenant(CreateTenantCommand command) {
        log.info("Creating tenant with name: {} and subdomain: {}",
                command.getName(), command.getSubdomain());

        // Create value objects
        Email email = new Email(command.getPrimaryEmail());
        Phone phone = new Phone(command.getPrimaryPhone());
        Address address = Address.builder()
                .street(command.getStreet())
                .neighborhood(command.getNeighborhood())
                .city(command.getCity())
                .province(command.getProvince())
                .postalCode(command.getPostalCode())
                .country(command.getCountry())
                .build();

        // Create tenant through domain service
        Tenant tenant = tenantDomainService.createTenant(
                command.getName(),
                command.getSubdomain(),
                email,
                phone,
                address
        );

        // Set business registration if provided
        if (command.getTaxId() != null || command.getRegistrationNumber() != null) {
            tenant.setBusinessRegistration(command.getTaxId(), command.getRegistrationNumber());
            tenantRepository.save(tenant);
        }

        log.info("Tenant created successfully with ID: {}", tenant.getId());
        return mapToDto(tenant);
    }

    public TenantDto updateTenant(String tenantId, UpdateTenantCommand command) {
        log.info("Updating tenant with ID: {}", tenantId);

        Tenant tenant = tenantRepository.findById(TenantId.of(tenantId))
                .orElseThrow(() -> EntityNotFoundException.forEntity(Tenant.class, tenantId));

        // Check if contact info needs updating
        boolean needsContactUpdate = false;
        Email email = tenant.getPrimaryEmail();
        Phone phone = tenant.getPrimaryPhone();
        Address address = tenant.getAddress();

        // Update email if provided
        if (command.getPrimaryEmail() != null && !command.getPrimaryEmail().isEmpty()) {
            email = new Email(command.getPrimaryEmail());
            needsContactUpdate = true;
        }

        // Update phone if provided
        if (command.getPrimaryPhone() != null && !command.getPrimaryPhone().isEmpty()) {
            phone = new Phone(command.getPrimaryPhone());
            needsContactUpdate = true;
        }

        // Update address if any address field is provided
        if (hasAddressUpdate(command)) {
            address = Address.builder()
                    .street(command.getStreet() != null ? command.getStreet() :
                            (tenant.getAddress() != null ? tenant.getAddress().getStreet() : null))
                    .neighborhood(command.getNeighborhood() != null ? command.getNeighborhood() :
                            (tenant.getAddress() != null ? tenant.getAddress().getNeighborhood() : null))
                    .city(command.getCity() != null ? command.getCity() :
                            (tenant.getAddress() != null ? tenant.getAddress().getCity() : ""))
                    .province(command.getProvince() != null ? command.getProvince() :
                            (tenant.getAddress() != null ? tenant.getAddress().getProvince() : null))
                    .postalCode(command.getPostalCode() != null ? command.getPostalCode() :
                            (tenant.getAddress() != null ? tenant.getAddress().getPostalCode() : null))
                    .country(command.getCountry() != null ? command.getCountry() :
                            (tenant.getAddress() != null ? tenant.getAddress().getCountry() : ""))
                    .build();
            needsContactUpdate = true;
        }

        // Apply contact updates if needed
        if (needsContactUpdate) {
            tenant.updateContactInfo(email, phone, address);
        }

        // Update business registration if provided
        if (command.getTaxId() != null || command.getRegistrationNumber() != null) {
            String taxId = command.getTaxId() != null ? command.getTaxId() : tenant.getTaxId();
            String registrationNumber = command.getRegistrationNumber() != null ?
                    command.getRegistrationNumber() : tenant.getRegistrationNumber();
            tenant.setBusinessRegistration(taxId, registrationNumber);
        }

        tenant = tenantRepository.save(tenant);
        log.info("Tenant updated successfully");
        return mapToDto(tenant);
    }

    private boolean hasAddressUpdate(UpdateTenantCommand command) {
        return command.getStreet() != null ||
                command.getNeighborhood() != null ||
                command.getCity() != null ||
                command.getProvince() != null ||
                command.getPostalCode() != null ||
                command.getCountry() != null;
    }

    public TenantDto activateTenant(String tenantId) {
        log.info("Activating tenant with ID: {}", tenantId);

        Tenant tenant = tenantRepository.findById(TenantId.of(tenantId))
                .orElseThrow(() -> EntityNotFoundException.forEntity(Tenant.class, tenantId));

        tenant.activate();
        tenant = tenantRepository.save(tenant);

        log.info("Tenant activated successfully");
        return mapToDto(tenant);
    }

    public TenantDto suspendTenant(String tenantId, String reason) {
        log.info("Suspending tenant with ID: {} for reason: {}", tenantId, reason);

        Tenant tenant = tenantRepository.findById(TenantId.of(tenantId))
                .orElseThrow(() -> EntityNotFoundException.forEntity(Tenant.class, tenantId));

        tenant.suspend(reason);
        tenant = tenantRepository.save(tenant);

        log.info("Tenant suspended successfully");
        return mapToDto(tenant);
    }

    public TenantDto reactivateTenant(String tenantId) {
        log.info("Reactivating tenant with ID: {}", tenantId);

        Tenant tenant = tenantRepository.findById(TenantId.of(tenantId))
                .orElseThrow(() -> EntityNotFoundException.forEntity(Tenant.class, tenantId));

        tenant.reactivate();
        tenant = tenantRepository.save(tenant);

        log.info("Tenant reactivated successfully");
        return mapToDto(tenant);
    }

    public TenantDto getTenantById(String tenantId) {
        return tenantRepository.findById(TenantId.of(tenantId))
                .map(this::mapToDto)
                .orElseThrow(() -> EntityNotFoundException.forEntity(Tenant.class, tenantId));
    }

    public TenantDto getTenantBySubdomain(String subdomain) {
        return tenantRepository.findBySubdomain(subdomain)
                .map(this::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Tenant", subdomain,
                        "No tenant found with subdomain"));
    }

    public List<TenantDto> getAllTenants() {
        return tenantRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TenantDto mapToDto(Tenant tenant) {
        return TenantDto.builder()
                .id(tenant.getId().toString())
                .name(tenant.getName())
                .subdomain(tenant.getSubdomain())
                .status(tenant.getStatus().name())
                .primaryEmail(tenant.getPrimaryEmail().getValue())
                .primaryPhone(tenant.getPrimaryPhone().getValue())
                .address(tenant.getAddress() != null ? tenant.getAddress().getFullAddress() : null)
                .taxId(tenant.getTaxId())
                .registrationNumber(tenant.getRegistrationNumber())
                .features(tenant.getFeatures())
                .maxUsers(tenant.getMaxUsers())
                .maxStudents(tenant.getMaxStudents())
                .createdAt(tenant.getCreatedAt())
                .activatedAt(tenant.getActivatedAt())
                .build();
    }
}