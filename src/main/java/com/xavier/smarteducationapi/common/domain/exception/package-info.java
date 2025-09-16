/**
 * Domain exception package.
 * 
 * Contains domain-layer exceptions representing business rule violations
 * and invalid domain states:
 * - SmartEducationException: Base exception for all system exceptions
 * - DomainException: Base class for domain-related exceptions  
 * - BusinessRuleViolationException: Business rule violations
 * - EntityNotFoundException: Entity lookup failures
 * - InvalidValueObjectException: Value object validation failures
 * - ConcurrencyException: Optimistic locking conflicts
 * 
 * All exceptions provide:
 * - Structured error information with error codes
 * - Correlation IDs for tracking
 * - Timestamps for debugging
 * - Contextual information (entity types, IDs, etc.)
 * 
 * This package is part of the common module's public API.
 * 
 * @author Xavier Nhagumbe
 */
@org.springframework.lang.NonNullApi
package com.xavier.smarteducationapi.common.domain.exception;
