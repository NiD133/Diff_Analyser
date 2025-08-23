package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test suite focuses on the functionality of the {@link JsonSetter.Value} class,
 * particularly its configuration merging logic.
 */
public class JsonSetterValueTest {

    /**
     * Tests that {@link JsonSetter.Value#merge(JsonSetter.Value, JsonSetter.Value)}
     * correctly prioritizes settings from the 'overrides' parameter over the 'base' parameter.
     *
     * This test verifies that when both a base and an override configuration define a property,
     * the value from the override configuration is used in the final merged result.
     */
    @Test
    public void mergeShouldPrioritizeOverrideValues() {
        // Arrange: Define a base configuration and an override configuration.
        // The base config has AS_EMPTY for both value and content nulls.
        JsonSetter.Value baseConfig = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);

        // The override config provides a different value for contentNulls (SKIP).
        JsonSetter.Value overrideConfig = JsonSetter.Value.construct(Nulls.AS_EMPTY, Nulls.SKIP);

        // Act: Merge the two configurations, with overrideConfig taking precedence.
        JsonSetter.Value mergedConfig = JsonSetter.Value.merge(baseConfig, overrideConfig);

        // Assert: The merged configuration should reflect the values from the override.
        // A new instance should be created because the configurations differ.
        assertNotSame("Merge should produce a new instance when overrides are applied",
                overrideConfig, mergedConfig);

        // The valueNulls is the same in both, but the final value should match the override.
        assertEquals("valueNulls should match the override's value",
                Nulls.AS_EMPTY, mergedConfig.getValueNulls());

        // The contentNulls is different and should be taken from the override.
        assertEquals("contentNulls should be taken from the override",
                Nulls.SKIP, mergedConfig.getContentNulls());
    }
}