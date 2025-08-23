package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link NumberOutput} class.
 */
public class NumberOutputTest {

    /**
     * Tests that {@link NumberOutput#notFinite(float)} correctly identifies a
     * standard finite float value and returns false.
     */
    @Test
    public void notFinite_shouldReturnFalse_forFiniteFloat() {
        // Arrange: A standard, finite float value.
        float finiteFloat = 2650.0F;

        // Act: Call the method under test.
        boolean result = NumberOutput.notFinite(finiteFloat);

        // Assert: The method should return false, indicating the number is finite.
        assertFalse("A regular float value should be considered finite.", result);
    }
}