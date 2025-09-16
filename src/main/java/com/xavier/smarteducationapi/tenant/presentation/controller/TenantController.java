package com.xavier.smarteducationapi.tenant.presentation.controller;

import com.xavier.smarteducationapi.tenant.application.command.*;
import com.xavier.smarteducationapi.tenant.application.dto.TenantDto;
import com.xavier.smarteducationapi.tenant.application.service.TenantApplicationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for tenant management.
 *
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
@Validated
public class TenantController {

    private final TenantApplicationService tenantApplicationService;

    @PostMapping
    public ResponseEntity<TenantDto> createTenant(@Valid @RequestBody CreateTenantCommand command) {
        TenantDto tenant = tenantApplicationService.createTenant(command);
        return new ResponseEntity<>(tenant, HttpStatus.CREATED);
    }

    @PutMapping("/{tenantId}")
    public ResponseEntity<TenantDto> updateTenant(
            @PathVariable String tenantId,
            @Valid @RequestBody UpdateTenantCommand command) {
        TenantDto tenant = tenantApplicationService.updateTenant(tenantId, command);
        return ResponseEntity.ok(tenant);
    }

    @PostMapping("/{tenantId}/activate")
    public ResponseEntity<TenantDto> activateTenant(@PathVariable String tenantId) {
        TenantDto tenant = tenantApplicationService.activateTenant(tenantId);
        return ResponseEntity.ok(tenant);
    }

    @PostMapping("/{tenantId}/suspend")
    public ResponseEntity<TenantDto> suspendTenant(
            @PathVariable String tenantId,
            @RequestParam String reason) {
        TenantDto tenant = tenantApplicationService.suspendTenant(tenantId, reason);
        return ResponseEntity.ok(tenant);
    }

    @PostMapping("/{tenantId}/reactivate")
    public ResponseEntity<TenantDto> reactivateTenant(@PathVariable String tenantId) {
        TenantDto tenant = tenantApplicationService.reactivateTenant(tenantId);
        return ResponseEntity.ok(tenant);
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<TenantDto> getTenant(@PathVariable String tenantId) {
        TenantDto tenant = tenantApplicationService.getTenantById(tenantId);
        return ResponseEntity.ok(tenant);
    }

    @GetMapping("/subdomain/{subdomain}")
    public ResponseEntity<TenantDto> getTenantBySubdomain(@PathVariable String subdomain) {
        TenantDto tenant = tenantApplicationService.getTenantBySubdomain(subdomain);
        return ResponseEntity.ok(tenant);
    }

    @GetMapping
    public ResponseEntity<List<TenantDto>> getAllTenants() {
        List<TenantDto> tenants = tenantApplicationService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }
}