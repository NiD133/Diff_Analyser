package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    /**
     * Verifies that doubleValue() correctly parses a string representing a simple integer.
     */
    @Test
    public void doubleValue_shouldReturnCorrectDoubleForIntegerString() {
        // Arrange: Create a LazilyParsedNumber with an integer string.
        LazilyParsedNumber number = new LazilyParsedNumber("3");
        double expectedValue = 3.0;

        // Act: Call the method under test.
        double actualValue = number.doubleValue();

        // Assert: Verify that the returned double matches the expected value.
        // A small delta is used to account for potential floating-point inaccuracies.
        assertEquals(expectedValue, actualValue, 0.0);
    }
}