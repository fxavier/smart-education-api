/**
 * Common shared components module.
 * 
 * This module contains shared components that can be used across other modules:
 * - Domain entities and value objects
 * - Domain events system
 * - Application events
 * - Infrastructure components
 * 
 * @author Xavier Nhagumbe
 */
@ApplicationModule(
    displayName = "Common Module",
    allowedDependencies = {}
)
package com.xavier.smarteducationapi.common;

import org.springframework.modulith.ApplicationModule;
