package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link JsonIncludeProperties.Value} class, focusing on
 * its merging and overriding logic.
 */
class JsonIncludePropertiesValueTest {

    // A test fixture class annotated with a specific set of included properties.
    @JsonIncludeProperties(value = { "foo", "bar" })
    private static class ClassWithIncludes {
    }

    /**
     * Tests that applying an "undefined" override (represented by {@code Value.all()})
     * to an existing {@code Value} instance does not alter the original set of
     * included properties.
     * <p>
     * According to the {@code withOverrides} contract, an undefined override should be
     * ignored, and the original value should be returned as-is.
     */
    @Test
    void withOverrides_whenOverrideIsUndefined_shouldReturnOriginalValue() {
        // Arrange
        // 1. Create a base Value from an annotation specifying {"foo", "bar"}
        JsonIncludeProperties.Value baseValue = JsonIncludeProperties.Value.from(
                ClassWithIncludes.class.getAnnotation(JsonIncludeProperties.class));

        // 2. The "undefined" override is represented by Value.all()
        JsonIncludeProperties.Value undefinedOverride = JsonIncludeProperties.Value.all();

        // 3. Define the expected outcome
        Set<String> expectedIncludedProperties = asSet("foo", "bar");

        // Act
        JsonIncludeProperties.Value resultValue = baseValue.withOverrides(undefinedOverride);
        Set<String> actualIncludedProperties = resultValue.getIncluded();

        // Assert
        assertEquals(expectedIncludedProperties, actualIncludedProperties,
                "Included properties should not change when the override is undefined.");
    }

    /**
     * Helper method to create a Set from a var-args array of strings.
     *
     * @param items The strings to include in the set.
     * @return A new LinkedHashSet containing the provided items.
     */
    private Set<String> asSet(String... items) {
        return new LinkedHashSet<>(Arrays.asList(items));
    }
}