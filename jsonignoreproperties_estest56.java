package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class, focusing on its merging logic.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * Tests that the {@code withOverrides} method correctly merges two {@code Value} instances.
     * The merging logic for boolean properties should behave like a logical OR,
     * where a {@code true} value in either instance results in {@code true} in the final merged instance.
     */
    @Test
    public void withOverridesShouldMergeBooleanPropertiesWithTruePrecedence() {
        // Arrange
        // Create a base configuration that allows setters and ignores unknown properties.
        final JsonIgnoreProperties.Value baseValue = JsonIgnoreProperties.Value
                .forIgnoreUnknown(true)
                .withAllowSetters();

        // Create an override configuration that only ignores unknown properties.
        // Note that its `allowSetters` property is implicitly false by default.
        final JsonIgnoreProperties.Value overrides = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Act
        // Apply the overrides to the base configuration. The expected merge logic for booleans is:
        // - ignoreUnknown: true (base) || true (overrides)  -> true
        // - allowSetters:  true (base) || false (overrides) -> true
        // - allowGetters:  false (base) || false (overrides) -> false
        // The resulting configuration should be equivalent to the baseValue.
        JsonIgnoreProperties.Value mergedValue = baseValue.withOverrides(overrides);

        // Assert
        // 1. The merge operation should produce a new, distinct instance.
        assertNotSame("A new Value instance should be created after applying overrides", baseValue, mergedValue);

        // 2. The merged instance should be logically equal to the base value because
        //    the 'true' in baseValue.allowSetters takes precedence over the 'false' in overrides.
        assertEquals("Merged value should be logically equal to the base value", baseValue, mergedValue);

        // 3. Explicitly verify the properties of the merged value for maximum clarity.
        assertTrue("ignoreUnknown should remain true after merge", mergedValue.getIgnoreUnknown());
        assertTrue("allowSetters should remain true as 'true' takes precedence", mergedValue.getAllowSetters());
        assertFalse("allowGetters should remain false as both were false", mergedValue.getAllowGetters());
    }
}