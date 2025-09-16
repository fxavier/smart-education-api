package com.xavier.smarteducationapi.common.application.exception;

import java.util.List;
import java.util.Map;

/**
 * Exception thrown when validation fails at the application layer.
 * 
 * This exception can contain multiple validation errors and provides
 * structured information about what validation rules were violated.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class ValidationException extends ApplicationException {
    
    private static final String ERROR_CODE = "VALIDATION_FAILED";
    
    private final Map<String, List<String>> validationErrors;
    
    public ValidationException(String message, Map<String, List<String>> validationErrors) {
        super(ERROR_CODE, buildMessage(message, validationErrors));
        this.validationErrors = validationErrors;
    }
    
    public ValidationException(Map<String, List<String>> validationErrors) {
        this("Validation failed", validationErrors);
    }
    
    public ValidationException(String field, String error) {
        this("Validation failed", Map.of(field, List.of(error)));
    }
    
    public Map<String, List<String>> getValidationErrors() {
        return validationErrors;
    }
    
    public boolean hasErrors() {
        return validationErrors != null && !validationErrors.isEmpty();
    }
    
    public int getErrorCount() {
        if (validationErrors == null) {
            return 0;
        }
        return validationErrors.values().stream()
                .mapToInt(List::size)
                .sum();
    }
    
    private static String buildMessage(String message, Map<String, List<String>> validationErrors) {
        if (validationErrors == null || validationErrors.isEmpty()) {
            return message;
        }
        
        StringBuilder sb = new StringBuilder(message);
        sb.append(". Validation errors: ");
        
        validationErrors.forEach((field, errors) -> {
            sb.append(String.format("[%s: %s] ", field, String.join(", ", errors)));
        });
        
        return sb.toString().trim();
    }
    
    /**
     * Builder class for constructing ValidationException with multiple errors.
     */
    public static class Builder {
        private final Map<String, List<String>> errors = new java.util.HashMap<>();
        
        public Builder addError(String field, String error) {
            errors.computeIfAbsent(field, k -> new java.util.ArrayList<>()).add(error);
            return this;
        }
        
        public Builder addErrors(String field, List<String> fieldErrors) {
            errors.computeIfAbsent(field, k -> new java.util.ArrayList<>()).addAll(fieldErrors);
            return this;
        }
        
        public ValidationException build() {
            return new ValidationException(errors);
        }
        
        public ValidationException build(String message) {
            return new ValidationException(message, errors);
        }
        
        public boolean hasErrors() {
            return !errors.isEmpty();
        }
    }
}
