package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link SerializedString} class, focusing on its exception-handling behavior.
 */
public class SerializedStringTest {

    /**
     * Verifies that {@link SerializedString#appendUnquotedUTF8(byte[], int)} throws a
     * {@link NullPointerException} when the provided buffer is null.
     */
    @Test(expected = NullPointerException.class)
    public void appendUnquotedUTF8ShouldThrowNullPointerExceptionForNullBuffer() {
        // Arrange: Create an instance of SerializedString. Its content is irrelevant for this test.
        SerializedString serializedString = new SerializedString("any-value");
        byte[] nullBuffer = null;
        int anyOffset = -1; // The offset value is not relevant to triggering the NPE.

        // Act: Call the method with a null buffer.
        // This action is expected to throw a NullPointerException.
        serializedString.appendUnquotedUTF8(nullBuffer, anyOffset);

        // Assert: The test passes if the expected NullPointerException is thrown.
        // This is handled declaratively by the @Test(expected=...) annotation.
    }
}