package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link JsonIncludeProperties.Value#withOverrides(JsonIncludeProperties.Value)} method.
 * This class focuses on how property inclusion rules are merged.
 */
@DisplayName("JsonIncludeProperties.Value#withOverrides")
class JsonIncludePropertiesValueMergeTest {

    // A test class annotated with a specific set of properties to include.
    @JsonIncludeProperties({"foo", "bar"})
    private static class ClassWithIncludes {
    }

    /**
     * Verifies that overriding with an empty set of properties results in an empty set.
     * The `withOverrides` method functions as an intersection. Therefore, the intersection
     * of any set of properties with an empty set should yield an empty set.
     */
    @Test
    @DisplayName("should result in an empty set when overridden by an empty set")
    void withOverrides_whenIntersectingWithEmptySet_shouldResultInEmptySet() {
        // Arrange:
        // 1. Create a base configuration from an annotation with {"foo", "bar"}.
        JsonIncludeProperties.Value baseValue = JsonIncludeProperties.Value.from(
                ClassWithIncludes.class.getAnnotation(JsonIncludeProperties.class));

        // 2. Create an overriding configuration with an empty set of properties.
        JsonIncludeProperties.Value emptyOverride = new JsonIncludeProperties.Value(Collections.emptySet());

        // Act: Apply the override.
        JsonIncludeProperties.Value resultValue = baseValue.withOverrides(emptyOverride);
        Set<String> resultingIncludedProperties = resultValue.getIncluded();

        // Assert: The resulting set of included properties should be empty.
        assertNotNull(resultingIncludedProperties, "The resulting set should not be null.");
        assertTrue(resultingIncludedProperties.isEmpty(),
                "Intersecting with an empty set of properties should produce an empty result.");
    }
}