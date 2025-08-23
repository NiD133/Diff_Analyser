package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link LazilyParsedNumber} class.
 */
public class LazilyParsedNumberTest {

    /**
     * Verifies that the {@code toString()} method returns the exact string
     * value that the object was constructed with.
     */
    @Test
    public void toString_shouldReturnTheOriginalStringValue() {
        // Arrange: Create a LazilyParsedNumber with a specific string value.
        // The original test used an empty string, which is a valid edge case.
        String originalValue = "";
        LazilyParsedNumber number = new LazilyParsedNumber(originalValue);

        // Act: Call the toString() method.
        String result = number.toString();

        // Assert: The returned string should be identical to the original.
        assertEquals(originalValue, result);
    }
}