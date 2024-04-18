package net.karmak.conference;


import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;
import org.mapstruct.Mapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.INTERFACES;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.DEPRECATED_API_SHOULD_NOT_BE_USED;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.testClassesShouldResideInTheSamePackageAsImplementation;

@AnalyzeClasses(packages = "net.karmak.conference", importOptions = {
    ImportOption.DoNotIncludeTests.class,
    ImportOption.DoNotIncludeJars.class,
    ImportOption.DoNotIncludeArchives.class
})
public class ArchitectureAndDesignRules {
    private static final String SERVICE = "service";
    private static final String SERVICE_IMPL = "serviceImpl";
    public static final String HTTP_ADAPTER = "httpAdapter";
    public static final String SQL_ADAPTER = "sqlAdapter";
    public static final String MAPPER = "mappers";
    public static final String VALIDATOR = "validator";
    public static final String ENTITY = "Entity";
    public static final String DTO = "DTO";

    @ArchTest
    ArchRule deprecatedApiShouldNotBeUsed = DEPRECATED_API_SHOULD_NOT_BE_USED;
    @ArchTest
    ArchRule fieldInjectionShouldNotBeUsed = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
    @ArchTest
    ArchRule genericExceptionsAreForbidden = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
    @ArchTest
    ArchRule standardOutputStreamsShouldNotBeUsed = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
    @ArchTest
    ArchRule testClassesShouldResideInTheSamePackage = testClassesShouldResideInTheSamePackageAsImplementation();


    @ArchTest
    ArchRule projectPackagesStructureRules = layeredArchitecture()
            .consideringAllDependencies()
            .as("Packages structure should match project hexagonal design rules")

            .layer(DTO).definedBy("..domain.dto..")
            .layer(ENTITY).definedBy("..domain.entity..")
            .layer(SERVICE).definedBy("..service..")
            .layer(SERVICE_IMPL).definedBy("..service.impl..")
            .layer(MAPPER).definedBy("..service.mapper..")
            .layer(VALIDATOR).definedBy("..service.validator..")
            .layer(HTTP_ADAPTER).definedBy("..adapter.http..")
            .layer(SQL_ADAPTER).definedBy("..adapter.sql..")

            .whereLayer(ENTITY).mayOnlyBeAccessedByLayers(SERVICE_IMPL, VALIDATOR, SQL_ADAPTER, MAPPER)
            .whereLayer(DTO).mayOnlyBeAccessedByLayers(SERVICE, SERVICE_IMPL, VALIDATOR, HTTP_ADAPTER, MAPPER)
            .whereLayer(SERVICE).mayOnlyBeAccessedByLayers(SERVICE_IMPL, HTTP_ADAPTER)
            .whereLayer(SERVICE_IMPL).mayNotBeAccessedByAnyLayer() // spring will manage implementations

            .whereLayer(HTTP_ADAPTER).mayNotBeAccessedByAnyLayer()
            .whereLayer(SQL_ADAPTER).mayOnlyBeAccessedByLayers(SERVICE_IMPL, VALIDATOR);

    @ArchTest
    ArchRule controllersShouldNotDependOnEachOther =
            classes().that().areAnnotatedWith(RestController.class)
                    .should().onlyDependOnClassesThat().areNotAnnotatedWith(RestController.class)
                    .as("Controllers should not depend on each other");

    @ArchTest
    ArchRule restControllersAreStatelessAndDependOnInterfaces =
            fields().that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
                    .should().beFinal()
                    .andShould().bePrivate()
                    .andShould().haveRawType(INTERFACES)
                    .as("Rest controllers are stateless and depend on interfaces");

    @ArchTest
    ArchRule restControllersShouldBeLocatedInHttpAdapter =
            classes().that().areAnnotatedWith(RestController.class)
                    .should().resideInAPackage("..adapter.http..")
                    .andShould().haveNameMatching(".*Controller")
                    .as("Rest controllers should reside in a http adapter package and have corresponding name suffix");

    @ArchTest
    ArchRule entitiesLocationRule =
            classes().that().areAnnotatedWith(Entity.class)
                    .should().resideInAPackage("..domain.entity..")
                    .as("Entities should reside in an entity package");

    @ArchTest
    ArchRule daoShouldBeLocatedInSqlAdaptor =
            classes().that().areInterfaces().and().areAssignableTo(CrudRepository.class)
                    .should().resideInAPackage("..adapter.sql..")
                    .andShould().haveNameMatching(".*Dao")
                    .as("Repositories should reside in a sql adapter package and have corresponding name suffix");

    @ArchTest
    ArchRule serviceImplementationsShouldBeLocatedInServiceImpl =
            classes().that().areAnnotatedWith(Service.class)
                    .should().resideInAPackage("..service.impl..")
                    .andShould().haveNameMatching(".*Basic.*Service")
                    .as("Service implementations should reside in a service impl package and have corresponding name");

    @ArchTest
    ArchRule mappersShouldBeLocatedInAppropriatePackage =
            classes().that().areAnnotatedWith(Mapper.class)
                    .should().resideInAPackage("..service.mapper..")
                    .andShould().haveNameMatching(".*Mapper")
                    .as("Mappers should reside in service.mapper package and have corresponding name");
}
