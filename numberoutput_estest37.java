package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on its
 * number-to-string conversion capabilities.
 */
public class NumberOutputTest {

    /**
     * Tests that converting a double with an integral value (like 1.0) to a string
     * correctly includes the decimal part when the 'fast writer' optimization is disabled.
     * This ensures the method falls back to standard Java double-to-string formatting.
     */
    @Test
    public void toString_shouldFormatIntegralDoubleWithDecimal_whenFastWriterIsDisabled() {
        // Arrange
        final double input = 1.0;
        final boolean useFastWriter = false;
        final String expectedOutput = "1.0";

        // Act
        final String actualOutput = NumberOutput.toString(input, useFastWriter);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
}