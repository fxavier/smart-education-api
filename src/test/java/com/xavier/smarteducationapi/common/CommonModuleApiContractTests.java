package com.xavier.smarteducationapi.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.xavier.smarteducationapi.common.api.CommonModuleApi;
import com.xavier.smarteducationapi.common.api.DomainEventApi;
import com.xavier.smarteducationapi.common.api.EntityApi;
import com.xavier.smarteducationapi.common.api.ValueObjectApi;
import com.xavier.smarteducationapi.common.domain.entity.AggregateRoot;
import com.xavier.smarteducationapi.common.domain.entity.BaseEntity;
import com.xavier.smarteducationapi.common.domain.event.DomainEvent;
import com.xavier.smarteducationapi.common.domain.event.DomainEventPublisher;
import com.xavier.smarteducationapi.common.domain.valueobject.Address;
import com.xavier.smarteducationapi.common.domain.valueobject.BaseId;
import com.xavier.smarteducationapi.common.domain.valueobject.Email;
import com.xavier.smarteducationapi.common.domain.valueobject.Money;
import com.xavier.smarteducationapi.common.domain.valueobject.Phone;
import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;

/**
 * API contract tests for the common module.
 * 
 * These tests verify that the public API of the common module is stable
 * and provides the expected contracts for other modules to use.
 * 
 * @author Xavier Nhagumbe
 */
class CommonModuleApiContractTests {

    @Test
    void commonModuleApiShouldProvideVersionAndModuleId() {
        // Test that the API provides basic module information
        assertNotNull(CommonModuleApi.API_VERSION);
        assertNotNull(CommonModuleApi.MODULE_ID);
        assertEquals("1.0", CommonModuleApi.API_VERSION);
        assertEquals("common", CommonModuleApi.MODULE_ID);
    }

    @Test
    void domainEventApiShouldDefineRequiredMethods() {
        // Verify that DomainEventApi interface has the required methods
        Method[] methods = DomainEventApi.class.getDeclaredMethods();
        List<String> methodNames = Arrays.stream(methods)
            .map(Method::getName)
            .toList();

        assertTrue(methodNames.contains("publishEvent"));
        assertTrue(methodNames.contains("publishEvents"));
        assertTrue(methodNames.contains("getEventPublisher"));
    }

    @Test
    void entityApiShouldDefineRequiredMethods() {
        // Verify that EntityApi interface has the required methods
        Method[] methods = EntityApi.class.getDeclaredMethods();
        List<String> methodNames = Arrays.stream(methods)
            .map(Method::getName)
            .toList();

        assertTrue(methodNames.contains("getBaseEntityClass"));
        assertTrue(methodNames.contains("getAggregateRootClass"));
        assertTrue(methodNames.contains("getBaseIdClass"));
    }

    @Test
    void valueObjectApiShouldDefineRequiredMethods() {
        // Verify that ValueObjectApi interface has the required methods
        Method[] methods = ValueObjectApi.class.getDeclaredMethods();
        List<String> methodNames = Arrays.stream(methods)
            .map(Method::getName)
            .toList();

        assertTrue(methodNames.contains("getAddressClass"));
        assertTrue(methodNames.contains("getEmailClass"));
        assertTrue(methodNames.contains("getMoneyClass"));
        assertTrue(methodNames.contains("getPhoneClass"));
        assertTrue(methodNames.contains("getTenantIdClass"));
    }

    @Test
    void domainEventInterfaceShouldBeAccessible() {
        // Test that the DomainEvent interface is accessible and has required methods
        assertNotNull(DomainEvent.class);
        
        Method[] methods = DomainEvent.class.getDeclaredMethods();
        List<String> methodNames = Arrays.stream(methods)
            .map(Method::getName)
            .toList();

        assertTrue(methodNames.contains("getEventId"));
        assertTrue(methodNames.contains("occurredOn"));
    }

    @Test
    void domainEventPublisherInterfaceShouldBeAccessible() {
        // Test that the DomainEventPublisher interface is accessible
        assertNotNull(DomainEventPublisher.class);
        
        Method[] methods = DomainEventPublisher.class.getDeclaredMethods();
        List<String> methodNames = Arrays.stream(methods)
            .map(Method::getName)
            .toList();

        assertTrue(methodNames.contains("publish"));
        assertTrue(methodNames.contains("publishAll"));
    }

    @Test
    void baseEntityShouldBeAccessible() {
        // Test that BaseEntity is accessible and properly structured
        assertNotNull(BaseEntity.class);
        assertTrue(Modifier.isAbstract(BaseEntity.class.getModifiers()));
        
        // Should have getId method
        Method[] methods = BaseEntity.class.getDeclaredMethods();
        boolean hasGetId = Arrays.stream(methods)
            .anyMatch(method -> "getId".equals(method.getName()));
        assertTrue(hasGetId);
    }

    @Test
    void aggregateRootShouldBeAccessible() {
        // Test that AggregateRoot is accessible and extends BaseEntity
        assertNotNull(AggregateRoot.class);
        assertTrue(Modifier.isAbstract(AggregateRoot.class.getModifiers()));
        assertTrue(BaseEntity.class.isAssignableFrom(AggregateRoot.class));
        
        // Should have domain event methods
        Method[] methods = AggregateRoot.class.getDeclaredMethods();
        List<String> methodNames = Arrays.stream(methods)
            .map(Method::getName)
            .toList();

        assertTrue(methodNames.contains("getUncommittedEvents"));
        assertTrue(methodNames.contains("markEventsAsCommitted"));
    }

    @Test
    void valueObjectsShouldBeAccessible() {
        // Test that all value objects are accessible
        assertNotNull(Address.class);
        assertNotNull(BaseId.class);
        assertNotNull(Email.class);
        assertNotNull(Money.class);
        assertNotNull(Phone.class);
        assertNotNull(TenantId.class);
    }

    @Test
    void baseIdShouldBeAbstractClass() {
        // Test that BaseId is properly structured as abstract
        assertNotNull(BaseId.class);
        assertTrue(Modifier.isAbstract(BaseId.class.getModifiers()));
    }

    @Test
    void tenantIdShouldExtendBaseId() {
        // Test that TenantId extends BaseId
        assertNotNull(TenantId.class);
        assertTrue(BaseId.class.isAssignableFrom(TenantId.class));
    }

    @Test
    void apiInterfacesShouldBeInterfaces() {
        // Test that all API classes are interfaces (contracts)
        assertTrue(CommonModuleApi.class.isInterface());
        assertTrue(DomainEventApi.class.isInterface());
        assertTrue(EntityApi.class.isInterface());
        assertTrue(ValueObjectApi.class.isInterface());
    }

    @Test
    void apiPackageShouldOnlyContainPublicContracts() {
        // Test that the API package follows naming conventions
        Package apiPackage = CommonModuleApi.class.getPackage();
        assertNotNull(apiPackage);
        assertEquals("com.xavier.smarteducationapi.common.api", apiPackage.getName());
    }
}
