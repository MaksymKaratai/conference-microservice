package net.karmak.conference;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@NoArgsConstructor
public class CamelCaseDisplayNameGenerator extends DisplayNameGenerator.Standard {
    private static final String NESTED_ICON = "\u21AA";

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        var className = super.generateDisplayNameForNestedClass(nestedClass);
        return NESTED_ICON + " " + splitByCamelCase(className)
                .collect(joining(" "));
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return splitByCamelCase(testMethod.getName())
                .map(String::toLowerCase)
                .collect(joining(" "));
    }

    private Stream<String> splitByCamelCase(String name) {
        return Arrays.stream(StringUtils.splitByCharacterTypeCamelCase(name));
    }
}
