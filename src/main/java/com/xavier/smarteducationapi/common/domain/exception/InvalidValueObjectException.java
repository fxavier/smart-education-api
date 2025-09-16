package com.xavier.smarteducationapi.common.domain.exception;

/**
 * Exception thrown when attempting to create a value object with invalid data.
 * 
 * Value objects must be immutable and always valid. This exception is thrown
 * when the provided data doesn't meet the value object's invariants.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class InvalidValueObjectException extends DomainException {
    
    private static final String ERROR_CODE = "INVALID_VALUE_OBJECT";
    
    private final String valueObjectType;
    private final Object invalidValue;
    private final String validationRule;
    
    public InvalidValueObjectException(String valueObjectType, Object invalidValue, String validationRule) {
        super(ERROR_CODE, String.format("Invalid value for %s: '%s'. Validation rule: %s", 
                valueObjectType, invalidValue, validationRule));
        this.valueObjectType = valueObjectType;
        this.invalidValue = invalidValue;
        this.validationRule = validationRule;
    }
    
    public InvalidValueObjectException(String valueObjectType, Object invalidValue, String validationRule, Throwable cause) {
        super(ERROR_CODE, String.format("Invalid value for %s: '%s'. Validation rule: %s", 
                valueObjectType, invalidValue, validationRule), cause);
        this.valueObjectType = valueObjectType;
        this.invalidValue = invalidValue;
        this.validationRule = validationRule;
    }
    
    public String getValueObjectType() {
        return valueObjectType;
    }
    
    public Object getInvalidValue() {
        return invalidValue;
    }
    
    public String getValidationRule() {
        return validationRule;
    }
    
    /**
     * Convenience method to create exception for a specific value object class.
     */
    public static InvalidValueObjectException forValueObject(Class<?> valueObjectClass, Object invalidValue, String rule) {
        return new InvalidValueObjectException(valueObjectClass.getSimpleName(), invalidValue, rule);
    }
}
