package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class, focusing on its core functionalities.
 */
public class SerializedStringTest {

    /**
     * Verifies that the charLength() method returns the correct character count
     * of the original string.
     */
    @Test
    public void charLengthShouldReturnLengthOfWrappedString() {
        // Arrange
        String originalString = "j";
        SerializedString serializedString = new SerializedString(originalString);
        int expectedLength = 1;

        // Act
        int actualLength = serializedString.charLength();

        // Assert
        assertEquals("The character length should match the original string's length.",
                expectedLength, actualLength);
    }
}