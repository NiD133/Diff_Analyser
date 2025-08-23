package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test suite focuses on the {@link JsonTypeInfo.Value} class, specifically testing
 * its helper methods.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that the static helper method {@code JsonTypeInfo.Value.isEnabled()}
     * correctly handles null input by returning false. This confirms that a null
     * {@code JsonTypeInfo.Value} is treated as "not enabled" for polymorphic handling.
     */
    @Test
    public void isEnabledShouldReturnFalseWhenValueIsNull() {
        // Act: Call the isEnabled method with a null argument.
        boolean result = JsonTypeInfo.Value.isEnabled(null);

        // Assert: Verify that the result is false, as expected for a null input.
        assertFalse("The isEnabled() method should return false when passed a null value.", result);
    }
}