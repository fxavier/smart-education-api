package com.xavier.smarteducationapi.common.infrastructure.exception;

/**
 * Exception thrown when database operations fail.
 * 
 * This includes connection failures, constraint violations, timeout errors,
 * and other database-related technical issues.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-16
 */
public class DatabaseException extends InfrastructureException {
    
    private static final String ERROR_CODE = "DATABASE_ERROR";
    
    private final String operation;
    private final String table;
    
    public DatabaseException(String message) {
        super(ERROR_CODE, message);
        this.operation = null;
        this.table = null;
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
        this.operation = null;
        this.table = null;
    }
    
    public DatabaseException(String operation, String table, String message) {
        super(ERROR_CODE, String.format("Database operation '%s' failed on table '%s': %s", 
                operation, table, message));
        this.operation = operation;
        this.table = table;
    }
    
    public DatabaseException(String operation, String table, String message, Throwable cause) {
        super(ERROR_CODE, String.format("Database operation '%s' failed on table '%s': %s", 
                operation, table, message), cause);
        this.operation = operation;
        this.table = table;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public String getTable() {
        return table;
    }
    
    /**
     * Convenience method for connection failures.
     */
    public static DatabaseException connectionFailed(String message, Throwable cause) {
        return new DatabaseException("Failed to connect to database: " + message, cause);
    }
    
    /**
     * Convenience method for constraint violations.
     */
    public static DatabaseException constraintViolation(String table, String constraint, Throwable cause) {
        return new DatabaseException("CONSTRAINT_VIOLATION", table, 
                "Constraint '" + constraint + "' violated", cause);
    }
}
