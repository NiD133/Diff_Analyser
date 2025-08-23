package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class, focusing on its
 * immutability and "wither" methods.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * Tests that the {@code withAllowGetters()} method creates a new, distinct
     * {@link JsonIgnoreProperties.Value} instance with the {@code allowGetters}
     * property set to true, while preserving all other properties from the original instance.
     * This also verifies the immutability of the {@code Value} objects.
     */
    @Test
    public void withAllowGettersShouldCreateNewInstanceWithGettersAllowedAndPreserveOtherProperties() {
        // Arrange: Create an initial Value instance with ignoreUnknown=true
        // and default values for other properties (e.g., allowGetters=false).
        JsonIgnoreProperties.Value originalValue = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Act: Create a new Value instance by calling the wither method.
        JsonIgnoreProperties.Value updatedValue = originalValue.withAllowGetters();

        // Assert: Verify the properties of both the original and the new instance.

        // 1. The original instance should be immutable and remain unchanged.
        assertFalse("Original value's allowGetters should remain false", originalValue.getAllowGetters());
        assertTrue("Original value's ignoreUnknown should remain true", originalValue.getIgnoreUnknown());

        // 2. The new instance should reflect the change and preserve other properties.
        assertTrue("Updated value's allowGetters should now be true", updatedValue.getAllowGetters());
        assertTrue("Updated value should preserve the ignoreUnknown property", updatedValue.getIgnoreUnknown());
        assertTrue("Updated value should preserve the default merge property", updatedValue.getMerge());
        assertFalse("Updated value should preserve the default allowSetters property", updatedValue.getAllowSetters());

        // 3. The two instances should be distinct and not equal.
        assertNotEquals("The new instance should not be equal to the original", originalValue, updatedValue);
    }
}