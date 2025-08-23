package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    @Test
    public void intValue_whenNumberIsZero_returnsZero() {
        // Arrange: Create a LazilyParsedNumber from the string "0".
        LazilyParsedNumber number = new LazilyParsedNumber("0");

        // Act: Get the integer value.
        int actualIntValue = number.intValue();

        // Assert: Verify that the integer value is 0.
        assertEquals(0, actualIntValue);
    }
}