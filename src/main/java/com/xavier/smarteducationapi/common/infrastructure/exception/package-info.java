/**
 * Infrastructure exception package.
 * 
 * Contains infrastructure-layer exceptions for technical failures:
 * - InfrastructureException: Base class for infrastructure-layer exceptions
 * - DatabaseException: Database operation failures and connection issues
 * - EventPublishingException: Domain event publishing failures
 * - ExternalServiceException: External service communication failures
 * 
 * These exceptions represent technical issues such as:
 * - Database connectivity problems
 * - Constraint violations
 * - Event publishing/messaging failures
 * - HTTP client errors and timeouts
 * - External service unavailability
 * 
 * Each exception provides specific context information relevant
 * to the type of failure (operation names, status codes, etc.).
 * 
 * This package is part of the common module's public API.
 * 
 * @author Xavier Nhagumbe
 */
@org.springframework.lang.NonNullApi
package com.xavier.smarteducationapi.common.infrastructure.exception;
