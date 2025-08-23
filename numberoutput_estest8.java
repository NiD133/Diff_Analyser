package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link NumberOutput} class, focusing on its string conversion methods.
 */
public class NumberOutputTest {

    /**
     * Verifies that the {@code toString(long)} method correctly converts a long
     * value that corresponds to the maximum integer value. This tests a common
     * boundary condition.
     */
    @Test
    public void shouldCorrectlyConvertIntegerMaxValueAsLongToString() {
        // Arrange: Define the input value and the expected string result.
        // Using Integer.MAX_VALUE makes the boundary condition explicit.
        long valueToConvert = Integer.MAX_VALUE;
        String expectedString = "2147483647";

        // Act: Call the method under test.
        String actualString = NumberOutput.toString(valueToConvert);

        // Assert: Verify the result is as expected.
        assertEquals(expectedString, actualString);
    }
}