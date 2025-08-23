package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link SerializedString} class, focusing on boundary conditions
 * and exception handling for the append methods.
 */
public class SerializedStringTest {

    /**
     * Verifies that appendUnquotedUTF8() throws an ArrayIndexOutOfBoundsException
     * when called with a negative offset. The method should not allow writing to
     * an invalid position in the buffer.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void appendUnquotedUTF8_shouldThrowException_whenOffsetIsNegative() {
        // Arrange
        SerializedString serializedString = new SerializedString("test-string");
        byte[] destinationBuffer = new byte[20];
        int invalidNegativeOffset = -1;

        // Act
        // This call is expected to throw an ArrayIndexOutOfBoundsException due to the negative offset.
        serializedString.appendUnquotedUTF8(destinationBuffer, invalidNegativeOffset);

        // Assert
        // The assertion is handled by the @Test(expected=...) annotation, which
        // fails the test if the expected exception is not thrown.
    }
}