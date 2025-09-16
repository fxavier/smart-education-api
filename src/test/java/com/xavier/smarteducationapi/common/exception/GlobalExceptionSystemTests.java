package com.xavier.smarteducationapi.common.exception;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.xavier.smarteducationapi.common.api.ExceptionApi;
import com.xavier.smarteducationapi.common.application.exception.ValidationException;
import com.xavier.smarteducationapi.common.domain.exception.BusinessRuleViolationException;
import com.xavier.smarteducationapi.common.domain.exception.EntityNotFoundException;
import com.xavier.smarteducationapi.common.domain.exception.SmartEducationException;
import com.xavier.smarteducationapi.common.infrastructure.exception.DatabaseException;

/**
 * Integration tests for the complete global exception system.
 * 
 * Validates that the entire exception hierarchy works correctly
 * and is properly accessible across the application.
 * 
 * @author Xavier Nhagumbe
 */
@SpringBootTest
class GlobalExceptionSystemTests {

    @Test
    void exceptionApiShouldProvideAccessToAllExceptionTypes() {
        // Given
        ExceptionApiImpl exceptionApi = new ExceptionApiImpl();
        
        // Then
        assertNotNull(exceptionApi.getBaseExceptionClass());
        assertNotNull(exceptionApi.getDomainExceptionClass());
        assertNotNull(exceptionApi.getApplicationExceptionClass());
        assertNotNull(exceptionApi.getInfrastructureExceptionClass());
        assertNotNull(exceptionApi.getBusinessRuleViolationExceptionClass());
        assertNotNull(exceptionApi.getEntityNotFoundExceptionClass());
        assertNotNull(exceptionApi.getValidationExceptionClass());
        assertNotNull(exceptionApi.getDatabaseExceptionClass());
    }
    
    @Test
    void exceptionsShouldBeThrowableAndCatchable() {
        // Test domain exception flow
        BusinessRuleViolationException domainException = assertThrows(
                BusinessRuleViolationException.class, 
                () -> { throw new BusinessRuleViolationException("TestRule", "Rule violated"); });
        
        assertTrue(domainException instanceof SmartEducationException);
        assertNotNull(domainException.getErrorCode());
        assertNotNull(domainException.getCorrelationId());
        
        // Test application exception flow
        ValidationException applicationException = assertThrows(
                ValidationException.class,
                () -> { throw new ValidationException("field", "error"); });
        
        assertTrue(applicationException instanceof SmartEducationException);
        assertTrue(applicationException.hasErrors());
        assertEquals(1, applicationException.getErrorCount());
        
        // Test infrastructure exception flow
        DatabaseException infrastructureException = assertThrows(
                DatabaseException.class,
                () -> { throw new DatabaseException("Connection failed"); });
        
        assertTrue(infrastructureException instanceof SmartEducationException);
        assertNotNull(infrastructureException.getErrorCode());
    }
    
    @Test
    void complexValidationExceptionScenario() {
        // Given
        ValidationException.Builder builder = new ValidationException.Builder()
                .addError("name", "Name is required")
                .addError("email", "Email is invalid")
                .addError("email", "Email already exists")
                .addError("phone", "Phone number is required")
                .addError("address.street", "Street is required")
                .addError("address.city", "City is required");
        
        // When
        ValidationException exception = builder.build("User creation failed");
        
        // Then
        assertTrue(exception.hasErrors());
        assertEquals(6, exception.getErrorCount());
        assertEquals(5, exception.getValidationErrors().size()); // 5 different fields
        
        // Verify specific field errors
        assertEquals(1, exception.getValidationErrors().get("name").size());
        assertEquals(2, exception.getValidationErrors().get("email").size());
        assertEquals(1, exception.getValidationErrors().get("phone").size());
        assertEquals(1, exception.getValidationErrors().get("address.street").size());
        assertEquals(1, exception.getValidationErrors().get("address.city").size());
        
        assertTrue(exception.getMessage().contains("User creation failed"));
    }
    
    @Test
    void exceptionChainingShouldWork() {
        // Given
        RuntimeException rootCause = new RuntimeException("Root cause");
        DatabaseException dbException = new DatabaseException("Database error", rootCause);
        
        // When
        EntityNotFoundException chainedException = new EntityNotFoundException("User", "123", dbException);
        
        // Then
        assertEquals(dbException, chainedException.getCause());
        assertEquals(rootCause, chainedException.getCause().getCause());
        
        // Verify that the original error information is preserved
        assertTrue(chainedException.getCause() instanceof DatabaseException);
        assertEquals("Database error", chainedException.getCause().getMessage());
    }
    
    @Test
    void factoryMethodsShouldProvideConsistentBehavior() {
        // Test EntityNotFoundException factory
        EntityNotFoundException entityNotFound = EntityNotFoundException.forEntity(User.class, 123L);
        assertEquals("User", entityNotFound.getEntityType());
        assertEquals(123L, entityNotFound.getEntityId());
        
        // Test DatabaseException factories
        DatabaseException connectionError = DatabaseException.connectionFailed("timeout", new RuntimeException());
        assertTrue(connectionError.getMessage().contains("Failed to connect to database"));
        
        DatabaseException constraintError = DatabaseException.constraintViolation("users", "email_unique", new RuntimeException());
        assertTrue(constraintError.getMessage().contains("Constraint"));
        assertTrue(constraintError.getMessage().contains("email_unique"));
    }
    
    @Test
    void emptyValidationExceptionBuilderShouldWork() {
        // Given
        ValidationException.Builder builder = new ValidationException.Builder();
        
        // When
        ValidationException exception = builder.build();
        
        // Then
        assertFalse(exception.hasErrors());
        assertEquals(0, exception.getErrorCount());
        assertTrue(exception.getValidationErrors().isEmpty());
    }
    
    @Test
    void builderShouldDetectErrors() {
        // Given
        ValidationException.Builder builder = new ValidationException.Builder();
        
        // Initially no errors
        assertFalse(builder.hasErrors());
        
        // After adding error
        builder.addError("field", "error");
        assertTrue(builder.hasErrors());
        
        // After adding multiple errors to same field
        builder.addErrors("field", List.of("error2", "error3"));
        assertTrue(builder.hasErrors());
    }
    
    // Test implementation of ExceptionApi for testing
    private static class ExceptionApiImpl implements ExceptionApi {
        @Override
        public Class<SmartEducationException> getBaseExceptionClass() {
            return SmartEducationException.class;
        }
        
        @Override
        public Class<com.xavier.smarteducationapi.common.domain.exception.DomainException> getDomainExceptionClass() {
            return com.xavier.smarteducationapi.common.domain.exception.DomainException.class;
        }
        
        @Override
        public Class<com.xavier.smarteducationapi.common.application.exception.ApplicationException> getApplicationExceptionClass() {
            return com.xavier.smarteducationapi.common.application.exception.ApplicationException.class;
        }
        
        @Override
        public Class<com.xavier.smarteducationapi.common.infrastructure.exception.InfrastructureException> getInfrastructureExceptionClass() {
            return com.xavier.smarteducationapi.common.infrastructure.exception.InfrastructureException.class;
        }
        
        @Override
        public Class<BusinessRuleViolationException> getBusinessRuleViolationExceptionClass() {
            return BusinessRuleViolationException.class;
        }
        
        @Override
        public Class<EntityNotFoundException> getEntityNotFoundExceptionClass() {
            return EntityNotFoundException.class;
        }
        
        @Override
        public Class<com.xavier.smarteducationapi.common.domain.exception.InvalidValueObjectException> getInvalidValueObjectExceptionClass() {
            return com.xavier.smarteducationapi.common.domain.exception.InvalidValueObjectException.class;
        }
        
        @Override
        public Class<com.xavier.smarteducationapi.common.domain.exception.ConcurrencyException> getConcurrencyExceptionClass() {
            return com.xavier.smarteducationapi.common.domain.exception.ConcurrencyException.class;
        }
        
        @Override
        public Class<com.xavier.smarteducationapi.common.application.exception.UseCaseExecutionException> getUseCaseExecutionExceptionClass() {
            return com.xavier.smarteducationapi.common.application.exception.UseCaseExecutionException.class;
        }
        
        @Override
        public Class<ValidationException> getValidationExceptionClass() {
            return ValidationException.class;
        }
        
        @Override
        public Class<DatabaseException> getDatabaseExceptionClass() {
            return DatabaseException.class;
        }
        
        @Override
        public Class<com.xavier.smarteducationapi.common.infrastructure.exception.EventPublishingException> getEventPublishingExceptionClass() {
            return com.xavier.smarteducationapi.common.infrastructure.exception.EventPublishingException.class;
        }
        
        @Override
        public Class<com.xavier.smarteducationapi.common.infrastructure.exception.ExternalServiceException> getExternalServiceExceptionClass() {
            return com.xavier.smarteducationapi.common.infrastructure.exception.ExternalServiceException.class;
        }
    }
    
    // Test helper class
    private static class User {}
}
