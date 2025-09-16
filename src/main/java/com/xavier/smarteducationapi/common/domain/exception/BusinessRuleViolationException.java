package com.xavier.smarteducationapi.common.domain.exception;

/**
 * Exception thrown when a business rule is violated.
 * 
 * Business rules are domain constraints that must always be satisfied.
 * This exception is thrown when an operation would violate these constraints.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class BusinessRuleViolationException extends DomainException {
    
    private static final String ERROR_CODE = "BUSINESS_RULE_VIOLATION";
    
    private final String ruleName;
    private final Object violatingValue;
    
    public BusinessRuleViolationException(String ruleName, String message) {
        super(ERROR_CODE, String.format("Business rule '%s' violated: %s", ruleName, message));
        this.ruleName = ruleName;
        this.violatingValue = null;
    }
    
    public BusinessRuleViolationException(String ruleName, String message, Object violatingValue) {
        super(ERROR_CODE, String.format("Business rule '%s' violated: %s. Violating value: %s", 
                ruleName, message, violatingValue));
        this.ruleName = ruleName;
        this.violatingValue = violatingValue;
    }
    
    public BusinessRuleViolationException(String ruleName, String message, Throwable cause) {
        super(ERROR_CODE, String.format("Business rule '%s' violated: %s", ruleName, message), cause);
        this.ruleName = ruleName;
        this.violatingValue = null;
    }
    
    public String getRuleName() {
        return ruleName;
    }
    
    public Object getViolatingValue() {
        return violatingValue;
    }
}
