package com.xavier.smarteducationapi.tenant.architecture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Architecture tests for the Tenant module.
 * 
 * Enforces architectural constraints and ensures proper
 * module boundaries and dependencies.
 * 
 * @author Xavier Nhagumbe
 */
@DisplayName("Tenant Module Architecture Tests")
class TenantModuleArchitectureTests {

    private final JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.xavier.smarteducationapi.tenant");

    @Test
    @DisplayName("Domain entities should reside in domain.entity package")
    void domainEntitiesShouldResideInDomainEntityPackage() {
        ArchRule rule = classes()
                .that().areAssignableTo("com.xavier.smarteducationapi.common.domain.entity.BaseEntity")
                .or().areAssignableTo("com.xavier.smarteducationapi.common.domain.entity.AggregateRoot")
                .and().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .should().resideInAPackage("..domain.entity..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Value objects should reside in domain.valueobject package")
    void valueObjectsShouldResideInDomainValueObjectPackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Status")
                .or().haveSimpleNameEndingWith("Plan")
                .or().haveSimpleNameEndingWith("Period")
                .and().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .should().resideInAPackage("..domain.valueobject..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain events should reside in domain.event package")
    void domainEventsShouldResideInDomainEventPackage() {
        ArchRule rule = classes()
                .that().areAssignableTo("com.xavier.smarteducationapi.common.domain.event.DomainEvent")
                .and().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .should().resideInAPackage("..domain.event..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain repositories should reside in domain.repository package")
    void domainRepositoriesShouldResideInDomainRepositoryPackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Repository")
                .and().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .and().areInterfaces()
                .and().haveSimpleNameNotEndingWith("JpaRepository")
                .should().resideInAPackage("..domain.repository..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain services should reside in domain.service package")
    void domainServicesShouldResideInDomainServicePackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("DomainService")
                .and().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .should().resideInAPackage("..domain.service..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Application services should reside in application.service package")
    void applicationServicesShouldResideInApplicationServicePackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("ApplicationService")
                .and().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .should().resideInAPackage("..application.service..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Commands should reside in application.command package")
    void commandsShouldResideInApplicationCommandPackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Command")
                .and().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .should().resideInAPackage("..application.command..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("DTOs should reside in application.dto package")
    void dtosShouldResideInApplicationDtoPackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Dto")
                .and().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .should().resideInAPackage("..application.dto..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers should reside in presentation package")
    void controllersShouldResideInPresentationPackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Controller")
                .and().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .should().resideInAPackage("..presentation..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("JPA entities should reside in infrastructure.persistence.entity package")
    void jpaEntitiesShouldResideInInfrastructurePersistenceEntityPackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("JpaEntity")
                .and().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .should().resideInAPackage("..infrastructure.persistence.entity..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain layer should not depend on application layer")
    void domainLayerShouldNotDependOnApplicationLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("..application..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain layer should not depend on infrastructure layer")
    void domainLayerShouldNotDependOnInfrastructureLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain layer should not depend on presentation layer")
    void domainLayerShouldNotDependOnPresentationLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("..presentation..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Application layer should not depend on infrastructure layer")
    void applicationLayerShouldNotDependOnInfrastructureLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Application layer should not depend on presentation layer")
    void applicationLayerShouldNotDependOnPresentationLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAPackage("..presentation..");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers should be annotated with @RestController or @Controller")
    void controllersShouldBeAnnotatedWithControllerAnnotations() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Controller")
                .and().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .should().beAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .orShould().beAnnotatedWith("org.springframework.stereotype.Controller");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Services should be annotated with @Service")
    void servicesShouldBeAnnotatedWithService() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Service")
                .and().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .should().beAnnotatedWith("org.springframework.stereotype.Service");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Repository implementations should be annotated with @Repository")
    void repositoryImplementationsShouldBeAnnotatedWithRepository() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("RepositoryImpl")
                .and().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .should().beAnnotatedWith("org.springframework.stereotype.Repository");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Tenant module should only depend on common module and standard libraries")
    void tenantModuleShouldOnlyDependOnCommonModule() {
        ArchRule rule = classes()
                .that().resideInAPackage("com.xavier.smarteducationapi.tenant..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                    "com.xavier.smarteducationapi.common..",
                    "com.xavier.smarteducationapi.tenant..",
                    "java..",
                    "javax..",
                    "jakarta..",
                    "org.springframework..",
                    "org.junit..",
                    "org.mockito..",
                    "org.assertj..",
                    "lombok..",
                    "org.slf4j..",
                    "com.tngtech.archunit.."
                );
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Entities should extend BaseEntity or AggregateRoot")
    void entitiesShouldExtendBaseEntityOrAggregateRoot() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain.entity..")
                .and().areNotInterfaces()
                .and().areNotEnums()
                .and().areNotAnnotations()
                .should().beAssignableTo("com.xavier.smarteducationapi.common.domain.entity.BaseEntity")
                .orShould().beAssignableTo("com.xavier.smarteducationapi.common.domain.entity.AggregateRoot");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain events should extend AbstractDomainEvent")
    void domainEventsShouldExtendAbstractDomainEvent() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain.event..")
                .and().haveSimpleNameEndingWith("Event")
                .should().beAssignableTo("com.xavier.smarteducationapi.common.domain.event.AbstractDomainEvent");
        
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Value objects should be enums or records")
    void valueObjectsShouldBeEnumsOrRecords() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain.valueobject..")
                .and().areNotInterfaces()
                .and().areNotAnnotations()
                .should().beEnums()
                .orShould().beRecords();
        
        rule.check(importedClasses);
    }
}
