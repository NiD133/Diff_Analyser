package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class, focusing on edge cases.
 */
public class SerializedStringTest {

    /**
     * Verifies that the appendUnquoted() method correctly handles an invalid (negative)
     * offset by returning -1, indicating that no characters were appended. This is the
     * expected behavior for invalid buffer offsets.
     */
    @Test
    public void appendUnquoted_shouldReturnNegativeOne_whenOffsetIsNegative() {
        // Arrange: Create a SerializedString, a destination buffer, and an invalid offset.
        SerializedString serializedString = new SerializedString("some-test-string");
        char[] destinationBuffer = new char[20];
        int invalidOffset = -1;

        // Act: Attempt to append the string's characters to the buffer with the invalid offset.
        int charsAppended = serializedString.appendUnquoted(destinationBuffer, invalidOffset);

        // Assert: The method should return -1 to signal that the operation failed due to
        // the invalid offset, rather than throwing an exception.
        assertEquals("Expected -1 for a negative offset", -1, charsAppended);
    }
}