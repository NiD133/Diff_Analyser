package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on floating-point number conversions.
 */
public class NumberOutputTest {

    /**
     * Tests that the {@link NumberOutput#toString(float)} method correctly converts a
     * negative float value into its string representation.
     */
    @Test
    public void shouldConvertNegativeFloatToString() {
        // Arrange: Define the input value and the expected result.
        final float inputValue = -310.243F;
        final String expectedOutput = "-310.243";

        // Act: Call the method under test.
        final String actualOutput = NumberOutput.toString(inputValue);

        // Assert: Verify the result is as expected.
        assertEquals(expectedOutput, actualOutput);
    }
}