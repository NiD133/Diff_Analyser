package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    @Test
    public void longValueShouldReturnCorrectValueForZeroString() {
        // Arrange: Create a LazilyParsedNumber instance with the string "0".
        LazilyParsedNumber number = new LazilyParsedNumber("0");

        // Act: Call the longValue() method to trigger parsing.
        long actualValue = number.longValue();

        // Assert: Verify that the parsed long value is 0.
        assertEquals(0L, actualValue);
    }
}