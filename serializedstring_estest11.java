package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Verifies that the charLength() method returns 0 for a SerializedString
     * created from an empty string.
     */
    @Test
    public void charLengthShouldReturnZeroForEmptyString() {
        // Arrange: Create a SerializedString instance with an empty string.
        SerializedString emptyString = new SerializedString("");

        // Act: Call the method under test to get the character length.
        int length = emptyString.charLength();

        // Assert: Verify that the returned length is 0.
        assertEquals("The character length of an empty string should be 0.", 0, length);
    }
}