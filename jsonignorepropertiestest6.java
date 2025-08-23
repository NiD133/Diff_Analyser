package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the merging and overriding logic of {@link JsonIgnoreProperties.Value}.
 * This suite focuses on how different configurations of {@code JsonIgnoreProperties.Value}
 * are combined using the {@code withOverrides} method.
 */
class JsonIgnorePropertiesValueMergeTest {

    private static final JsonIgnoreProperties.Value EMPTY = JsonIgnoreProperties.Value.empty();

    @Test
    void mergeWithMergeEnabledShouldCombineProperties() {
        // Arrange: Create a base config and an override that allows merging.
        JsonIgnoreProperties.Value baseConfig = EMPTY
                .withIgnoreUnknown()
                .withAllowGetters();
        JsonIgnoreProperties.Value mergingOverrides = EMPTY
                .withMerge()
                .withIgnored("a");

        // Act: Apply the overrides to the base configuration.
        JsonIgnoreProperties.Value result = baseConfig.withOverrides(mergingOverrides);

        // Assert: The result should be a union of the properties.
        // Property from 'mergingOverrides' is applied.
        assertEquals(Collections.singleton("a"), result.getIgnored());
        // Properties from 'baseConfig' are retained.
        assertTrue(result.getIgnoreUnknown());
        assertTrue(result.getAllowGetters());
        // Default properties remain unchanged.
        assertFalse(result.getAllowSetters());
    }

    @Test
    void mergeWithMergeDisabledShouldReplaceProperties() {
        // Arrange: Create a base config and an override that disables merging.
        JsonIgnoreProperties.Value baseConfig = EMPTY
                .withIgnoreUnknown()
                .withAllowGetters();
        JsonIgnoreProperties.Value replacingOverrides = EMPTY.withoutMerge();

        // Act: Apply the overrides to the base configuration.
        JsonIgnoreProperties.Value result = baseConfig.withOverrides(replacingOverrides);

        // Assert: The result should be identical to the override, as merging is disabled.
        assertEquals(replacingOverrides, result);

        // Explicitly verify the properties of the result match 'replacingOverrides'.
        assertEquals(Collections.emptySet(), result.getIgnored());
        assertFalse(result.getIgnoreUnknown());
        assertFalse(result.getAllowGetters());
        assertFalse(result.getAllowSetters());
    }

    @Test
    void withOverridesShouldReturnSameInstanceForNullOverride() {
        // Arrange
        JsonIgnoreProperties.Value config = EMPTY.withIgnored("a");

        // Act & Assert
        assertSame(config, config.withOverrides(null),
            "Applying a null override should not change the original instance.");
    }

    @Test
    void withOverridesShouldReturnSameInstanceForEmptyOverride() {
        // Arrange
        JsonIgnoreProperties.Value config = EMPTY.withIgnored("a");

        // Act & Assert
        assertSame(config, config.withOverrides(EMPTY),
            "Applying an empty override should not change the original instance.");
    }
}