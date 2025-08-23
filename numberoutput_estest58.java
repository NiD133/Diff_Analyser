package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on its number-to-string
 * conversion capabilities.
 */
public class NumberOutputTest {

    /**
     * Verifies that the {@code toString(float, boolean)} method correctly formats a
     * whole number float (e.g., 1.0F) when the 'useFastWriter' option is enabled.
     * The expected result should retain the decimal part, like "1.0".
     */
    @Test
    public void toStringWithFastWriterShouldCorrectlyFormatWholeNumberFloat() {
        // Arrange: Define the input value and the expected string output.
        float inputValue = 1.0F;
        boolean useFastWriter = true;
        String expectedOutput = "1.0";

        // Act: Call the method under test.
        String actualOutput = NumberOutput.toString(inputValue, useFastWriter);

        // Assert: Verify that the actual output matches the expected value.
        assertEquals(expectedOutput, actualOutput);
    }
}