package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class.
 */
public class NumberOutputTest {

    @Test
    public void toString_shouldConvertPositiveIntegerToString() {
        // Arrange: Define the input number and the expected string output.
        final int number = 1;
        final String expected = "1";

        // Act: Call the method under test.
        final String actual = NumberOutput.toString(number);

        // Assert: Verify that the actual output matches the expected output.
        assertEquals(expected, actual);
    }
}