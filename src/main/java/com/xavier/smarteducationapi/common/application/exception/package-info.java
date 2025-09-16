/**
 * Application exception package.
 * 
 * Contains application-layer exceptions for use case failures and
 * application service errors:
 * - ApplicationException: Base class for application-layer exceptions
 * - UseCaseExecutionException: Use case execution failures
 * - ValidationException: Input validation failures with detailed error information
 * 
 * These exceptions typically wrap domain exceptions or represent
 * application-specific error conditions like validation failures.
 * 
 * ValidationException includes a builder pattern for constructing
 * exceptions with multiple field-level validation errors.
 * 
 * This package is part of the common module's public API.
 * 
 * @author Xavier Nhagumbe
 */
@org.springframework.lang.NonNullApi
package com.xavier.smarteducationapi.common.application.exception;
