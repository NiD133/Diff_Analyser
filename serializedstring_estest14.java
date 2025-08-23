package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Verifies that appending an empty SerializedString to a character buffer
     * results in zero characters being written.
     */
    @Test
    public void appendUnquoted_shouldReturnZero_whenStringIsEmpty() {
        // Arrange: Create an empty SerializedString and a destination buffer.
        SerializedString emptyString = new SerializedString("");
        char[] destinationBuffer = new char[10];
        int offset = 0;

        // Act: Attempt to append the empty string's unquoted value to the buffer.
        int charsAppended = emptyString.appendUnquoted(destinationBuffer, offset);

        // Assert: Verify that the method reports that zero characters were appended.
        assertEquals("Appending an empty string should result in 0 characters being written.", 0, charsAppended);
    }
}