package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link SerializedString} class, focusing on exception-handling scenarios.
 */
public class SerializedStringTest {

    /**
     * Verifies that calling {@code appendQuotedUTF8} with a negative offset
     * throws an {@link ArrayIndexOutOfBoundsException}.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void appendQuotedUTF8_shouldThrowException_whenOffsetIsNegative() {
        // Arrange: Create a SerializedString and a destination buffer.
        SerializedString serializedString = new SerializedString("any-string-value");
        byte[] destinationBuffer = new byte[100];
        int invalidOffset = -1;

        // Act: Attempt to append the string's data at a negative offset.
        // The @Test(expected=...) annotation will assert that the expected exception is thrown.
        serializedString.appendQuotedUTF8(destinationBuffer, invalidOffset);
    }
}