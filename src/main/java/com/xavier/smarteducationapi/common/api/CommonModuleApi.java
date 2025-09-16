package com.xavier.smarteducationapi.common.api;

/**
 * Marker interface for the Common Module API.
 * 
 * This interface serves as a documentation and discovery mechanism
 * for the common module's public API. All public interfaces and classes
 * that are part of the module's API should reference or be documented
 * in relation to this interface.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-15
 */
public interface CommonModuleApi {
    
    /**
     * Version of the Common Module API.
     */
    String API_VERSION = "1.0";
    
    /**
     * Module identifier for the common module.
     */
    String MODULE_ID = "common";
    
}
