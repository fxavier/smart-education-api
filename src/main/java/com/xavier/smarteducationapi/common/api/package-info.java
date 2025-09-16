/**
 * Common module API package.
 * 
 * This package contains the public API of the common module that should be
 * accessible to other modules. It serves as a facade to the internal
 * implementations and provides a clear contract for inter-module communication.
 * 
 * Key components:
 * - Domain event interfaces and abstractions
 * - Base entity and value object types
 * - Domain event publisher interfaces
 * - Common application services
 * 
 * All other modules should only depend on this API package, not on internal
 * implementation packages.
 * 
 * @author Xavier Nhagumbe
 */
@org.springframework.lang.NonNullApi
package com.xavier.smarteducationapi.common.api;
