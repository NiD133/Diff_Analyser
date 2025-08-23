package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link LazilyParsedNumber}.
 */
public class LazilyParsedNumberTest {

    /**
     * Verifies that the longValue() method correctly parses a string
     * representing a valid long integer.
     */
    @Test
    public void longValueShouldReturnCorrectLongForIntegerString() {
        // Arrange: Create a LazilyParsedNumber with a simple integer string.
        LazilyParsedNumber number = new LazilyParsedNumber("3");
        long expectedLong = 3L;

        // Act: Call the method under test.
        long actualLong = number.longValue();

        // Assert: Verify that the returned long matches the expected value.
        assertEquals(expectedLong, actualLong);
    }
}