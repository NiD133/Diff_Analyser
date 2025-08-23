package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.nio.ByteBuffer;

/**
 * Contains tests for the {@link SerializedString} class.
 * This version is refactored for improved clarity and maintainability.
 */
public class SerializedStringTest {

    /**
     * Verifies that calling putUnquotedUTF8 with a null ByteBuffer
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void putUnquotedUTF8_whenBufferIsNull_shouldThrowNullPointerException() {
        // Arrange: Create a SerializedString instance. The actual string content
        // is not relevant for this null-check test.
        SerializedString serializedString = new SerializedString("test-string");

        // Act: Attempt to write the string to a null ByteBuffer.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        serializedString.putUnquotedUTF8((ByteBuffer) null);
    }
}