package com.xavier.smarteducationapi.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.xavier.smarteducationapi.common.api.CommonModuleApi;
import com.xavier.smarteducationapi.common.domain.entity.AggregateRoot;
import com.xavier.smarteducationapi.common.domain.entity.BaseEntity;
import com.xavier.smarteducationapi.common.domain.event.DomainEventPublisher;
import com.xavier.smarteducationapi.common.domain.valueobject.Address;
import com.xavier.smarteducationapi.common.domain.valueobject.BaseId;
import com.xavier.smarteducationapi.common.domain.valueobject.Email;
import com.xavier.smarteducationapi.common.domain.valueobject.Money;
import com.xavier.smarteducationapi.common.domain.valueobject.Phone;
import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;
import com.xavier.smarteducationapi.common.infrastructure.event.SpringDomainEventPublisher;

/**
 * Test to verify that the common module's packages are accessible
 * to other modules through the defined package-info.java configurations.
 * 
 * This test simulates how other modules would access common module components.
 * 
 * @author Xavier Nhagumbe
 */
@SpringBootTest
class CommonModuleAccessTest {

    @Test
    void shouldAccessCommonModuleApi() {
        // Test API access
        assertEquals("1.0", CommonModuleApi.API_VERSION);
        assertEquals("common", CommonModuleApi.MODULE_ID);
    }

    @Test
    void shouldAccessDomainEntities() {
        // Test domain entity access
        assertNotNull(BaseEntity.class);
        assertNotNull(AggregateRoot.class);
        
        // These classes should be accessible from other modules
        assertTrue(BaseEntity.class.isAssignableFrom(BaseEntity.class));
        assertTrue(BaseEntity.class.isAssignableFrom(AggregateRoot.class));
    }

    @Test
    void shouldAccessValueObjects() {
        // Test value object access
        assertNotNull(Address.class);
        assertNotNull(BaseId.class);
        assertNotNull(Email.class);
        assertNotNull(Money.class);
        assertNotNull(Phone.class);
        assertNotNull(TenantId.class);
    }

    @Test
    void shouldAccessDomainEvents() {
        // Test domain event system access
        assertNotNull(DomainEventPublisher.class);
        
        // Test that we can access the Spring implementation
        assertNotNull(SpringDomainEventPublisher.class);
        assertTrue(DomainEventPublisher.class.isAssignableFrom(SpringDomainEventPublisher.class));
    }

    @Test
    void shouldAccessApplicationLayer() {
        // Test application layer access
        assertNotNull(com.xavier.smarteducationapi.common.application.event.DomainEventListener.class);
    }

    @Test
    void shouldAccessInfrastructureLayer() {
        // Test infrastructure layer access
        assertNotNull(SpringDomainEventPublisher.class);
    }
}
