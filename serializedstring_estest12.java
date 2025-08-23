package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Tests that calling {@code asQuotedChars()} on a SerializedString
     * created with an empty string returns an empty character array.
     */
    @Test
    public void asQuotedChars_withEmptyString_returnsEmptyCharArray() {
        // Arrange: Create a SerializedString instance with an empty string.
        SerializedString emptySerializedString = new SerializedString("");

        // Act: Get the JSON-quoted character representation.
        char[] result = emptySerializedString.asQuotedChars();

        // Assert: The resulting character array should have a length of 0.
        assertEquals("The quoted char array for an empty string should be empty", 0, result.length);
    }
}