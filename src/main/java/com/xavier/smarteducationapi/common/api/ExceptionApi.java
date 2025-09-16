package com.xavier.smarteducationapi.common.api;

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
 * API facade for the common module's exception handling system.
 * 
 * This interface provides access to the global exception hierarchy
 * that other modules can use for consistent error handling across
 * the entire application.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public interface ExceptionApi {
    
    /**
     * Gets the base exception class for all system exceptions.
     * 
     * @return the SmartEducationException class
     */
    Class<SmartEducationException> getBaseExceptionClass();
    
    /**
     * Gets the domain exception base class.
     * 
     * @return the DomainException class
     */
    Class<DomainException> getDomainExceptionClass();
    
    /**
     * Gets the application exception base class.
     * 
     * @return the ApplicationException class
     */
    Class<ApplicationException> getApplicationExceptionClass();
    
    /**
     * Gets the infrastructure exception base class.
     * 
     * @return the InfrastructureException class
     */
    Class<InfrastructureException> getInfrastructureExceptionClass();
    
    /**
     * Gets the business rule violation exception class.
     * 
     * @return the BusinessRuleViolationException class
     */
    Class<BusinessRuleViolationException> getBusinessRuleViolationExceptionClass();
    
    /**
     * Gets the entity not found exception class.
     * 
     * @return the EntityNotFoundException class
     */
    Class<EntityNotFoundException> getEntityNotFoundExceptionClass();
    
    /**
     * Gets the invalid value object exception class.
     * 
     * @return the InvalidValueObjectException class
     */
    Class<InvalidValueObjectException> getInvalidValueObjectExceptionClass();
    
    /**
     * Gets the concurrency exception class.
     * 
     * @return the ConcurrencyException class
     */
    Class<ConcurrencyException> getConcurrencyExceptionClass();
    
    /**
     * Gets the use case execution exception class.
     * 
     * @return the UseCaseExecutionException class
     */
    Class<UseCaseExecutionException> getUseCaseExecutionExceptionClass();
    
    /**
     * Gets the validation exception class.
     * 
     * @return the ValidationException class
     */
    Class<ValidationException> getValidationExceptionClass();
    
    /**
     * Gets the database exception class.
     * 
     * @return the DatabaseException class
     */
    Class<DatabaseException> getDatabaseExceptionClass();
    
    /**
     * Gets the event publishing exception class.
     * 
     * @return the EventPublishingException class
     */
    Class<EventPublishingException> getEventPublishingExceptionClass();
    
    /**
     * Gets the external service exception class.
     * 
     * @return the ExternalServiceException class
     */
    Class<ExternalServiceException> getExternalServiceExceptionClass();
    
}
