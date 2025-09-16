package com.xavier.smarteducationapi.common.exception;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.xavier.smarteducationapi.common.infrastructure.exception.ExternalServiceException;
import com.xavier.smarteducationapi.common.infrastructure.exception.InfrastructureException;

/**
 * Tests for the exception hierarchy and common functionality.
 * 
 * Verifies that all exceptions inherit properly from the base classes
 * and provide the expected functionality like error codes, timestamps,
 * and correlation IDs.
 * 
 * @author Xavier Nhagumbe
 */
class ExceptionHierarchyTests {

    @Test
    void smartEducationExceptionShouldProvideBaseFields() {
        // Given
        TestException exception = new TestException("TEST_ERROR", "Test message");
        
        // Then
        assertEquals("TEST_ERROR", exception.getErrorCode());
        assertEquals("Test message", exception.getMessage());
        assertNotNull(exception.getTimestamp());
        assertNotNull(exception.getCorrelationId());
        assertTrue(exception.getTimestamp().isBefore(Instant.now().plusSeconds(1)));
    }
    
    @Test
    void domainExceptionShouldInheritFromSmartEducationException() {
        // Given
        BusinessRuleViolationException exception = new BusinessRuleViolationException(
                "TestRule", "Rule violated");
        
        // Then
        assertTrue(exception instanceof SmartEducationException);
        assertTrue(exception instanceof DomainException);
        assertNotNull(exception.getErrorCode());
        assertNotNull(exception.getCorrelationId());
    }
    
    @Test
    void applicationExceptionShouldInheritFromSmartEducationException() {
        // Given
        UseCaseExecutionException exception = new UseCaseExecutionException(
                "TestUseCase", "Use case failed");
        
        // Then
        assertTrue(exception instanceof SmartEducationException);
        assertTrue(exception instanceof ApplicationException);
        assertNotNull(exception.getErrorCode());
        assertNotNull(exception.getCorrelationId());
    }
    
    @Test
    void infrastructureExceptionShouldInheritFromSmartEducationException() {
        // Given
        DatabaseException exception = new DatabaseException("Database connection failed");
        
        // Then
        assertTrue(exception instanceof SmartEducationException);
        assertTrue(exception instanceof InfrastructureException);
        assertNotNull(exception.getErrorCode());
        assertNotNull(exception.getCorrelationId());
    }
    
    @Test
    void entityNotFoundExceptionShouldProvideEntityInfo() {
        // Given
        String entityType = "User";
        UUID entityId = UUID.randomUUID();
        EntityNotFoundException exception = new EntityNotFoundException(entityType, entityId);
        
        // Then
        assertEquals(entityType, exception.getEntityType());
        assertEquals(entityId, exception.getEntityId());
        assertTrue(exception.getMessage().contains(entityType));
        assertTrue(exception.getMessage().contains(entityId.toString()));
    }
    
    @Test
    void invalidValueObjectExceptionShouldProvideValidationInfo() {
        // Given
        String valueObjectType = "Email";
        String invalidValue = "invalid-email";
        String validationRule = "Must be a valid email format";
        
        InvalidValueObjectException exception = new InvalidValueObjectException(
                valueObjectType, invalidValue, validationRule);
        
        // Then
        assertEquals(valueObjectType, exception.getValueObjectType());
        assertEquals(invalidValue, exception.getInvalidValue());
        assertEquals(validationRule, exception.getValidationRule());
    }
    
    @Test
    void concurrencyExceptionShouldProvideVersionInfo() {
        // Given
        String entityType = "User";
        UUID entityId = UUID.randomUUID();
        Long expectedVersion = 1L;
        Long actualVersion = 2L;
        
        ConcurrencyException exception = new ConcurrencyException(
                entityType, entityId, expectedVersion, actualVersion);
        
        // Then
        assertEquals(entityType, exception.getEntityType());
        assertEquals(entityId, exception.getEntityId());
        assertEquals(expectedVersion, exception.getExpectedVersion());
        assertEquals(actualVersion, exception.getActualVersion());
    }
    
    @Test
    void validationExceptionShouldSupportMultipleErrors() {
        // Given
        ValidationException.Builder builder = new ValidationException.Builder()
                .addError("name", "Name is required")
                .addError("email", "Email is invalid")
                .addError("email", "Email already exists");
        
        ValidationException exception = builder.build();
        
        // Then
        assertTrue(exception.hasErrors());
        assertEquals(3, exception.getErrorCount());
        
        Map<String, List<String>> errors = exception.getValidationErrors();
        assertEquals(2, errors.size());
        assertEquals(1, errors.get("name").size());
        assertEquals(2, errors.get("email").size());
    }
    
    @Test
    void externalServiceExceptionShouldProvideServiceInfo() {
        // Given
        String serviceName = "PaymentService";
        String operation = "processPayment";
        Integer statusCode = 500;
        String message = "Internal server error";
        
        ExternalServiceException exception = new ExternalServiceException(
                serviceName, operation, statusCode, message);
        
        // Then
        assertEquals(serviceName, exception.getServiceName());
        assertEquals(operation, exception.getOperation());
        assertEquals(statusCode, exception.getStatusCode());
    }
    
    @Test
    void convenienceMethodsShouldWork() {
        // Test EntityNotFoundException convenience method
        EntityNotFoundException entityNotFound = EntityNotFoundException.forEntity(User.class, 123L);
        assertEquals("User", entityNotFound.getEntityType());
        assertEquals(123L, entityNotFound.getEntityId());
        
        // Test InvalidValueObjectException convenience method
        InvalidValueObjectException invalidVO = InvalidValueObjectException.forValueObject(
                Email.class, "invalid", "Must be valid email");
        assertEquals("Email", invalidVO.getValueObjectType());
        assertEquals("invalid", invalidVO.getInvalidValue());
        
        // Test UseCaseExecutionException convenience method
        UseCaseExecutionException useCaseError = UseCaseExecutionException.forUseCase(
                CreateUserUseCase.class, "Failed to create user");
        assertEquals("CreateUserUseCase", useCaseError.getUseCaseName());
    }
    
    // Test helper classes
    private static class TestException extends SmartEducationException {
        public TestException(String errorCode, String message) {
            super(errorCode, message);
        }
    }
    
    private static class User {}
    private static class Email {}
    private static class CreateUserUseCase {}
}
