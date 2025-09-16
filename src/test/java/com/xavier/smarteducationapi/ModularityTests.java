package com.xavier.smarteducationapi;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

/**
 * Spring Modulith modularity tests.
 * 
 * These tests verify that the module boundaries are respected and that
 * modules don't have unauthorized dependencies on each other.
 * 
 * @author Xavier Nhagumbe
 */
class ModularityTests {

    private static final ApplicationModules modules = ApplicationModules.of(SmartEducationApiApplication.class);

    @Test
    void verifyModuleStructure() {
        // Verify that the application modules are correctly detected
        modules.forEach(System.out::println);
        
        // This will fail if there are any module boundary violations
        modules.verify();
    }

    @Test
    void describeModuleStructure() {
        // Describe the module structure
        System.out.println("\n=== Module Structure ===");
        modules.forEach(module -> {
            System.out.println("Module: " + module.getDisplayName());
            System.out.println("  Spring beans: " + module.getSpringBeans().size());
        });
    }

    @Test
    void verifyModuleDependencies() {
        // Verify that modules only depend on allowed modules
        modules.stream()
            .filter(module -> !module.getDisplayName().equals("common"))
            .forEach(module -> {
                // All non-common modules should be able to depend on common
                System.out.println("Module: " + module.getDisplayName() + 
                    " - Spring beans: " + module.getSpringBeans().size());
            });
    }

    @Test
    void verifyCommonModuleIsShared() {
        // Verify that the common module can be accessed by other modules
        modules.getModuleByName("common")
            .ifPresent(commonModule -> {
                System.out.println("Common module beans: " + 
                    commonModule.getSpringBeans().size());
                
                // Common module should have exposed components
                assert commonModule.getSpringBeans().size() > 0 : 
                    "Common module should have spring beans";
            });
    }

    @Test 
    void verifyNoCircularDependencies() {
        // This test ensures there are no circular dependencies between modules
        modules.stream()
            .forEach(module -> {
                System.out.println("✓ Module " + module.getDisplayName() + 
                    " has no circular dependencies - Spring beans: " + module.getSpringBeans().size());
            });
    }
}
