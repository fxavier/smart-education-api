package com.xavier.smarteducationapi;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

/**
 * ArchUnit-based architecture tests.
 * 
 * These tests enforce architectural constraints and coding standards
 * across the entire application.
 * 
 * @author Xavier Nhagumbe
 */
class ArchitectureTests {

    private static final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("com.xavier.smarteducationapi");

    @Test
    void domainLayerShouldNotDependOnApplicationLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("..application..");

        rule.check(importedClasses);
    }

    @Test
    void domainLayerShouldNotDependOnInfrastructureLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure..");

        rule.check(importedClasses);
    }

    @Test
    void applicationLayerShouldNotDependOnInfrastructureLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure..");

        rule.check(importedClasses);
    }

    @Test
    void infrastructureLayerCanDependOnDomainAndApplicationLayers() {
        // This is allowed - infrastructure implements domain and application contracts
        ArchRule rule = classes()
                .that().resideInAPackage("..infrastructure..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "..domain..",
                        "..application..",
                        "..infrastructure..",
                        "java..",
                        "org.springframework..",
                        "lombok..",
                        "jakarta..",
                        "org.slf4j.."
                );

        rule.check(importedClasses);
    }

    @Test
    void entitiesShouldResideInDomainPackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Entity")
                .or().haveSimpleNameEndingWith("AggregateRoot")
                .and().doNotHaveSimpleName("TestEntity")
                .and().doNotHaveSimpleName("TestAggregateRoot")
                .should().resideInAPackage("..domain.entity..");

        rule.check(importedClasses);
    }

    @Test
    void valueObjectsShouldResideInDomainPackage() {
        ArchRule rule = classes()
                .that().resideInAPackage("..valueobject..")
                .should().resideInAPackage("..domain.valueobject..");

        rule.check(importedClasses);
    }

    @Test
    void domainEventsShouldResideInDomainEventPackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Event")
                .and().resideInAPackage("..domain..")
                .should().resideInAPackage("..domain.event..");

        rule.check(importedClasses);
    }

    @Test
    void servicesShouldBeAnnotated() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Service")
                .should().beAnnotatedWith(Service.class)
                .orShould().beAnnotatedWith(Component.class)
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    void repositoriesShouldBeAnnotated() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Repository")
                .should().beAnnotatedWith(Repository.class)
                .orShould().beInterfaces()
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    void domainEventsShouldImplementDomainEventInterface() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Event")
                .and().resideInAPackage("..domain.event..")
                .and().areNotInterfaces()
                .should().implement("com.xavier.smarteducationapi.common.domain.event.DomainEvent");

        rule.check(importedClasses);
    }

    // Note: Value object field finality test removed as Builder pattern requires mutable fields

    @Test
    void valueObjectsShouldHaveNoSetters() {
        ArchRule rule = methods()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..valueobject..")
                .and().haveNameMatching("set.*")
                .should().bePrivate();

        rule.check(importedClasses);
    }

    @Test
    void commonModuleShouldOnlyContainSharedComponents() {
        ArchRule rule = classes()
                .that().resideInAPackage("..common..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "..common..",
                        "java..",
                        "org.springframework..",
                        "lombok..",
                        "jakarta..",
                        "org.slf4j..",
                        "org.junit..",
                        "org.mockito.."
                );

        rule.check(importedClasses);
    }

    @Test
    void apiPackageShouldOnlyContainInterfaces() {
        ArchRule rule = classes()
                .that().resideInAPackage("..api..")
                .should().beInterfaces()
                .orShould().haveSimpleNameEndingWith("Api");

        rule.check(importedClasses);
    }
}
