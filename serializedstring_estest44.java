package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Tests for the {@link SerializedString} class, focusing on invalid argument handling.
 */
public class SerializedStringTest {

    /**
     * Verifies that calling {@code appendUnquoted} with a negative offset
     * correctly throws an {@link ArrayIndexOutOfBoundsException}.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void appendUnquoted_withNegativeOffset_shouldThrowArrayIndexOutOfBoundsException() {
        // Arrange: Create a SerializedString and a destination buffer.
        // The actual string content and buffer are not relevant to the exception being tested.
        SerializedString serializedString = new SerializedString("test value");
        char[] destinationBuffer = new char[20];
        int invalidNegativeOffset = -1;

        // Act: Attempt to append the string to the buffer at a negative offset.
        // The @Test(expected=...) annotation asserts that the correct exception is thrown.
        serializedString.appendUnquoted(destinationBuffer, invalidNegativeOffset);
    }
}