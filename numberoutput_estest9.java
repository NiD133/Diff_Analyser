package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on its number-to-string conversion methods.
 */
public class NumberOutputTest {

    /**
     * Tests that {@link NumberOutput#toString(long)} correctly converts a small negative long value
     * into its string representation.
     */
    @Test
    public void toString_withNegativeLong_shouldReturnCorrectStringRepresentation() {
        // Arrange: Define the input value and the expected result.
        long negativeValue = -11L;
        String expectedString = "-11";

        // Act: Call the method under test.
        String actualString = NumberOutput.toString(negativeValue);

        // Assert: Verify that the actual result matches the expected result.
        assertEquals(expectedString, actualString);
    }
}