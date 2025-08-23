package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    @Test
    public void longValue_whenGivenNegativeIntegerString_returnsCorrectLong() {
        // Arrange: Create a LazilyParsedNumber with a string representing a negative integer.
        String negativeNumberString = "-6";
        LazilyParsedNumber number = new LazilyParsedNumber(negativeNumberString);
        long expectedLong = -6L;

        // Act: Call the longValue() method to get the parsed long.
        long actualLong = number.longValue();

        // Assert: Verify that the returned long value matches the expected value.
        assertEquals(expectedLong, actualLong);
    }
}