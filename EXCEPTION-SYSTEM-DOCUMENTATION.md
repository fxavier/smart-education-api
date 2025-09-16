# Global Exception System Documentation

## Overview

The Smart Education API implements a comprehensive global exception system in the `common` module that provides consistent error handling across all modules. The system is designed to be hierarchical, informative, and easy to use.

## Architecture

### Exception Hierarchy

```
SmartEducationException (Base)
├── DomainException
│   ├── BusinessRuleViolationException
│   ├── EntityNotFoundException
│   ├── InvalidValueObjectException
│   └── ConcurrencyException
├── ApplicationException
│   ├── UseCaseExecutionException
│   └── ValidationException
└── InfrastructureException
    ├── DatabaseException
    ├── EventPublishingException
    └── ExternalServiceException
```

## Key Features

### 1. **Structured Error Information**

All exceptions provide:

- **Error Code**: Unique identifier for the error type
- **Timestamp**: When the error occurred
- **Correlation ID**: For request tracing across services
- **Context Information**: Relevant details about the failure

### 2. **Layer-Specific Exceptions**

#### Domain Layer

- **BusinessRuleViolationException**: Business rule violations
- **EntityNotFoundException**: Entity lookup failures
- **InvalidValueObjectException**: Value object validation errors
- **ConcurrencyException**: Optimistic locking conflicts

#### Application Layer

- **UseCaseExecutionException**: Use case execution failures
- **ValidationException**: Input validation with detailed field-level errors

#### Infrastructure Layer

- **DatabaseException**: Database operation failures
- **EventPublishingException**: Event publishing failures
- **ExternalServiceException**: External service communication errors

## Usage Examples

### Basic Exception Throwing

```java
// Domain exception
throw new EntityNotFoundException("User", userId);

// Application exception with validation
ValidationException.Builder builder = new ValidationException.Builder()
    .addError("email", "Email is required")
    .addError("password", "Password too weak");
throw builder.build();

// Infrastructure exception
throw new DatabaseException("SELECT", "users", "Connection timeout");
```

### Factory Methods

```java
// Entity not found with convenience method
throw EntityNotFoundException.forEntity(User.class, 123L);

// Database connection failure
throw DatabaseException.connectionFailed("timeout", cause);

// External service timeout
throw ExternalServiceException.timeout("PaymentService", "processPayment", cause);
```

### Exception Chaining

```java
try {
    // Some database operation
} catch (SQLException ex) {
    throw new DatabaseException("Failed to save entity", ex);
}
```

## Package Structure

```
src/main/java/com/xavier/smarteducationapi/common/
├── domain/exception/
│   ├── SmartEducationException.java
│   ├── DomainException.java
│   ├── BusinessRuleViolationException.java
│   ├── EntityNotFoundException.java
│   ├── InvalidValueObjectException.java
│   └── ConcurrencyException.java
├── application/exception/
│   ├── ApplicationException.java
│   ├── UseCaseExecutionException.java
│   └── ValidationException.java
├── infrastructure/exception/
│   ├── InfrastructureException.java
│   ├── DatabaseException.java
│   ├── EventPublishingException.java
│   └── ExternalServiceException.java
└── api/
    └── ExceptionApi.java
```

## Accessibility

All exception packages are properly exposed through:

- **Package-info.java files**: Enable Spring Modulith access
- **ExceptionApi interface**: Provides programmatic access to exception classes
- **Global accessibility**: Any module can import and use these exceptions

## Testing

### Test Categories

1. **ExceptionHierarchyTests**: Verifies inheritance and base functionality
2. **ExceptionAccessibilityTests**: Ensures all exceptions are importable
3. **GlobalExceptionSystemTests**: Integration tests for complete system

### Test Commands

```bash
# Test exception hierarchy
./mvnw test -Dtest=ExceptionHierarchyTests

# Test accessibility
./mvnw test -Dtest=ExceptionAccessibilityTests

# Test complete system
./mvnw test -Dtest=GlobalExceptionSystemTests
```

## ValidationException Builder Pattern

The `ValidationException` supports a fluent builder pattern for complex validation scenarios:

```java
ValidationException exception = new ValidationException.Builder()
    .addError("name", "Name is required")
    .addError("email", "Email format is invalid")
    .addError("email", "Email already exists")
    .addErrors("address", List.of("Street is required", "City is required"))
    .build("User registration failed");

// Access validation details
Map<String, List<String>> errors = exception.getValidationErrors();
int totalErrors = exception.getErrorCount();
boolean hasErrors = exception.hasErrors();
```

## Error Information Access

All exceptions provide rich contextual information:

```java
try {
    // Some operation
} catch (SmartEducationException ex) {
    String errorCode = ex.getErrorCode();
    Instant timestamp = ex.getTimestamp();
    String correlationId = ex.getCorrelationId();

    if (ex instanceof EntityNotFoundException enf) {
        String entityType = enf.getEntityType();
        Object entityId = enf.getEntityId();
    }

    if (ex instanceof ValidationException ve) {
        Map<String, List<String>> errors = ve.getValidationErrors();
    }
}
```

## Best Practices

### 1. **Use Appropriate Exception Types**

- Domain exceptions for business rule violations
- Application exceptions for use case failures
- Infrastructure exceptions for technical issues

### 2. **Provide Meaningful Context**

- Include entity types and IDs for not found exceptions
- Add field names and rules for validation exceptions
- Specify operations and tables for database exceptions

### 3. **Use Factory Methods**

- Leverage convenience methods like `EntityNotFoundException.forEntity()`
- Use specific factory methods like `DatabaseException.connectionFailed()`

### 4. **Exception Chaining**

- Always chain root causes when wrapping exceptions
- Preserve original stack traces for debugging

### 5. **Correlation IDs**

- Use correlation IDs for distributed tracing
- Pass correlation IDs when creating exceptions in request contexts

## Integration with Other Modules

Other modules can use the exception system by:

1. **Importing exception classes**:

```java
import com.xavier.smarteducationapi.common.domain.exception.EntityNotFoundException;
```

2. **Using the ExceptionApi**:

```java
@Autowired
private ExceptionApi exceptionApi;

Class<EntityNotFoundException> entityNotFound = exceptionApi.getEntityNotFoundExceptionClass();
```

3. **Following the established patterns**:

- Extend base exceptions for module-specific errors
- Use the same error code and correlation ID patterns
- Provide similar contextual information

## Performance Considerations

- Exception creation includes timestamp and UUID generation
- Correlation IDs are automatically generated but can be provided
- Builder pattern for ValidationException minimizes object creation
- Factory methods provide optimized exception creation paths

## Future Extensions

The system is designed to support:

- Custom error codes per module
- Internationalization of error messages
- Integration with monitoring and alerting systems
- Structured logging with correlation IDs
- Metrics collection for error patterns

---

**Author**: Xavier Nhagumbe  
**Version**: 1.0  
**Date**: September 16, 2025
