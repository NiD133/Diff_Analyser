package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link JsonIgnoreProperties.Value} class, focusing on its static merge logic.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * Verifies that when merging a null 'base' value with a non-null 'overrides' value,
     * the merge method returns the 'overrides' instance directly.
     */
    @Test
    public void merge_shouldReturnOverridesInstance_whenBaseIsNull() {
        // Arrange: Define a non-null 'overrides' configuration and a null 'base'.
        JsonIgnoreProperties.Value overrides = JsonIgnoreProperties.Value.forIgnoredProperties("prop1", "prop2");
        JsonIgnoreProperties.Value base = null;

        // Act: Perform the merge operation.
        JsonIgnoreProperties.Value result = JsonIgnoreProperties.Value.merge(base, overrides);

        // Assert: The result should be the exact same instance as the 'overrides' object,
        // not just an equal one.
        assertSame("When the base value is null, the overrides instance should be returned.", overrides, result);

        // Further Assert: Confirm the properties of the returned instance are correct.
        assertFalse("ignoreUnknown should be false by default", result.getIgnoreUnknown());
        assertFalse("allowGetters should be false by default", result.getAllowGetters());
        assertFalse("allowSetters should be false by default", result.getAllowSetters());
        assertTrue("merge should be true by default", result.getMerge());
    }
}