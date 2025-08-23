package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link JsonIncludeProperties.Value}.
 * This focuses on the merging and overriding logic.
 */
class JsonIncludePropertiesTestTest5 {

    /**
     * Helper class annotated to provide a base {@link JsonIncludeProperties.Value}
     * for testing purposes.
     */
    @JsonIncludeProperties(value = {"foo", "bar"})
    private static class BasePropertiesConfig {
    }

    @Test
    void withOverrides_shouldIntersectIncludedProperties() {
        // Arrange: Define the base and override configurations.
        // The withOverrides() method is expected to compute the intersection of properties
        // when both the base and override values have properties defined.

        // 1. Base value is created from an annotation with properties {"foo", "bar"}
        JsonIncludeProperties.Value baseValue = JsonIncludeProperties.Value.from(
                BasePropertiesConfig.class.getAnnotation(JsonIncludeProperties.class));

        // 2. Override value is created with a single property, {"foo"}
        JsonIncludeProperties.Value overrideValue = new JsonIncludeProperties.Value(asSet("foo"));

        // Act: Apply the override.
        JsonIncludeProperties.Value resultValue = baseValue.withOverrides(overrideValue);
        Set<String> actualIncludedProperties = resultValue.getIncluded();

        // Assert: Verify that the result contains only the properties present in BOTH sets.
        // The intersection of {"foo", "bar"} and {"foo"} should be {"foo"}.
        Set<String> expectedIncludedProperties = asSet("foo");
        assertEquals(expectedIncludedProperties, actualIncludedProperties);
    }

    /**
     * Creates a Set of strings for easy definition of expected values.
     */
    private Set<String> asSet(String... args) {
        return new LinkedHashSet<>(Arrays.asList(args));
    }
}