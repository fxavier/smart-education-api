package com.xavier.smarteducationapi.common.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.xavier.smarteducationapi.common.application.exception.ApplicationException;
import com.xavier.smarteducationapi.common.application.exception.UseCaseExecutionException;
import com.xavier.smarteducationapi.common.application.exception.ValidationException;
import com.xavier.smarteducationapi.common.domain.exception.BusinessRuleViolationException;
import com.xavier.smarteducationapi.common.domain.exception.ConcurrencyException;
import com.xavier.smarteducationapi.common.domain.exception.DomainException;
import com.xavier.smarteducationapi.common.domain.exception.EntityNotFoundException;
import com.xavier.smarteducationapi.common.domain.exception.InvalidValueObjectException;
import com.xavier.smarteducationapi.common.domain.exception.SmartEducationException;
import com.xavier.smarteducationapi.common.infrastructure.exception.DatabaseException;
import com.xavier.smarteducationapi.common.infrastructure.exception.EventPublishingException;
import com.xavier.smarteducationapi.common.infrastructure.exception.ExternalServiceException;
import com.xavier.smarteducationapi.common.infrastructure.exception.InfrastructureException;

/**
 * Tests to verify that all exception classes are accessible from other modules.
 * 
 * This test simulates how other modules would import and use the common
 * module's exception classes. All exceptions should be accessible through
 * the exposed packages.
 * 
 * @author Xavier Nhagumbe
 */
class ExceptionAccessibilityTests {

    @Test
    void baseExceptionsShouldBeAccessible() {
        // Test that base exception classes can be accessed
        assertNotNull(SmartEducationException.class);
        assertNotNull(DomainException.class);
        assertNotNull(ApplicationException.class);
        assertNotNull(InfrastructureException.class);
        
        // Verify inheritance hierarchy
        assertTrue(DomainException.class.getSuperclass().equals(SmartEducationException.class));
        assertTrue(ApplicationException.class.getSuperclass().equals(SmartEducationException.class));
        assertTrue(InfrastructureException.class.getSuperclass().equals(SmartEducationException.class));
    }
    
    @Test
    void domainExceptionsShouldBeAccessible() {
        // Test that all domain exception classes can be accessed and instantiated
        assertNotNull(BusinessRuleViolationException.class);
        assertNotNull(EntityNotFoundException.class);
        assertNotNull(InvalidValueObjectException.class);
        assertNotNull(ConcurrencyException.class);
        
        // Test instantiation
        BusinessRuleViolationException businessRule = new BusinessRuleViolationException("TestRule", "Test message");
        assertNotNull(businessRule);
        
        EntityNotFoundException entityNotFound = new EntityNotFoundException("TestEntity", "123");
        assertNotNull(entityNotFound);
        
        InvalidValueObjectException invalidVO = new InvalidValueObjectException("TestVO", "invalid", "Test rule");
        assertNotNull(invalidVO);
        
        ConcurrencyException concurrency = new ConcurrencyException("TestEntity", "123", 1L, 2L);
        assertNotNull(concurrency);
    }
    
    @Test
    void applicationExceptionsShouldBeAccessible() {
        // Test that all application exception classes can be accessed and instantiated
        assertNotNull(UseCaseExecutionException.class);
        assertNotNull(ValidationException.class);
        
        // Test instantiation
        UseCaseExecutionException useCaseError = new UseCaseExecutionException("TestUseCase", "Test message");
        assertNotNull(useCaseError);
        
        ValidationException validation = new ValidationException("field", "error message");
        assertNotNull(validation);
        
        // Test ValidationException builder
        ValidationException.Builder builder = new ValidationException.Builder();
        assertNotNull(builder);
        
        ValidationException builtValidation = builder
                .addError("field1", "error1")
                .addError("field2", "error2")
                .build();
        assertNotNull(builtValidation);
    }
    
    @Test
    void infrastructureExceptionsShouldBeAccessible() {
        // Test that all infrastructure exception classes can be accessed and instantiated
        assertNotNull(DatabaseException.class);
        assertNotNull(EventPublishingException.class);
        assertNotNull(ExternalServiceException.class);
        
        // Test instantiation
        DatabaseException dbError = new DatabaseException("Test database error");
        assertNotNull(dbError);
        
        EventPublishingException eventError = new EventPublishingException("Test event publishing error");
        assertNotNull(eventError);
        
        ExternalServiceException serviceError = new ExternalServiceException("TestService", "Test service error");
        assertNotNull(serviceError);
    }
    
    @Test
    void convenienceFactoryMethodsShouldBeAccessible() {
        // Test that all convenience factory methods can be accessed and used
        
        // EntityNotFoundException factory methods
        EntityNotFoundException entityNotFound = EntityNotFoundException.forEntity(String.class, "123");
        assertNotNull(entityNotFound);
        
        // InvalidValueObjectException factory methods
        InvalidValueObjectException invalidVO = InvalidValueObjectException.forValueObject(
                String.class, "invalid", "test rule");
        assertNotNull(invalidVO);
        
        // UseCaseExecutionException factory methods
        UseCaseExecutionException useCaseError = UseCaseExecutionException.forUseCase(
                String.class, "test message");
        assertNotNull(useCaseError);
        
        // ConcurrencyException factory methods
        ConcurrencyException concurrencyError = ConcurrencyException.forEntity(
                String.class, "123", 1L, 2L);
        assertNotNull(concurrencyError);
        
        // DatabaseException factory methods
        DatabaseException dbConnectionError = DatabaseException.connectionFailed(
                "connection failed", new RuntimeException());
        assertNotNull(dbConnectionError);
        
        DatabaseException constraintViolation = DatabaseException.constraintViolation(
                "users", "unique_email", new RuntimeException());
        assertNotNull(constraintViolation);
        
        // ExternalServiceException factory methods
        ExternalServiceException timeout = ExternalServiceException.timeout(
                "TestService", "testOperation", new RuntimeException());
        assertNotNull(timeout);
        
        ExternalServiceException authFailed = ExternalServiceException.authenticationFailed(
                "TestService", "testOperation");
        assertNotNull(authFailed);
        
        ExternalServiceException serviceUnavailable = ExternalServiceException.serviceUnavailable(
                "TestService", new RuntimeException());
        assertNotNull(serviceUnavailable);
    }
    
    @Test
    void exceptionFieldsShouldBeAccessible() {
        // Test that all exception-specific fields can be accessed
        
        BusinessRuleViolationException businessRule = new BusinessRuleViolationException(
                "TestRule", "Test message", "violating value");
        assertEquals("TestRule", businessRule.getRuleName());
        assertEquals("violating value", businessRule.getViolatingValue());
        
        EntityNotFoundException entityNotFound = new EntityNotFoundException("TestEntity", "123");
        assertEquals("TestEntity", entityNotFound.getEntityType());
        assertEquals("123", entityNotFound.getEntityId());
        
        InvalidValueObjectException invalidVO = new InvalidValueObjectException(
                "TestVO", "invalid", "test rule");
        assertEquals("TestVO", invalidVO.getValueObjectType());
        assertEquals("invalid", invalidVO.getInvalidValue());
        assertEquals("test rule", invalidVO.getValidationRule());
        
        ConcurrencyException concurrency = new ConcurrencyException("TestEntity", "123", 1L, 2L);
        assertEquals("TestEntity", concurrency.getEntityType());
        assertEquals("123", concurrency.getEntityId());
        assertEquals(Long.valueOf(1L), concurrency.getExpectedVersion());
        assertEquals(Long.valueOf(2L), concurrency.getActualVersion());
        
        UseCaseExecutionException useCaseError = new UseCaseExecutionException("TestUseCase", "test message");
        assertEquals("TestUseCase", useCaseError.getUseCaseName());
        
        DatabaseException dbError = new DatabaseException("SELECT", "users", "query failed");
        assertEquals("SELECT", dbError.getOperation());
        assertEquals("users", dbError.getTable());
        
        ExternalServiceException serviceError = new ExternalServiceException(
                "TestService", "testOp", 500, "server error");
        assertEquals("TestService", serviceError.getServiceName());
        assertEquals("testOp", serviceError.getOperation());
        assertEquals(Integer.valueOf(500), serviceError.getStatusCode());
    }
    
    private static void assertEquals(Object expected, Object actual) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
    }
}
