package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on its number-to-string
 * conversion capabilities.
 */
public class NumberOutputTest {

    /**
     * Tests that {@link NumberOutput#toString(float, boolean)} correctly converts a negative float
     * to its string representation when the "fast writer" optimization is disabled.
     */
    @Test
    public void toString_shouldConvertNegativeFloat_whenFastWriterIsDisabled() {
        // Arrange
        final float inputValue = -2304.1F;
        final String expectedString = "-2304.1";
        final boolean useFastWriter = false;

        // Act
        final String actualString = NumberOutput.toString(inputValue, useFastWriter);

        // Assert
        assertEquals(expectedString, actualString);
    }
}