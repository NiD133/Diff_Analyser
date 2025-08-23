package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link SerializedString} class, focusing on its handling of invalid arguments.
 */
public class SerializedStringTest {

    /**
     * Verifies that the {@code appendQuotedUTF8} method throws a {@code NullPointerException}
     * when the provided destination buffer is null. This is the expected behavior, as the
     * method cannot write to a non-existent buffer.
     */
    @Test(expected = NullPointerException.class)
    public void appendQuotedUTF8_whenBufferIsNull_shouldThrowNullPointerException() {
        // Arrange: Create a SerializedString instance. The actual string content is not
        // relevant for this test case, as the check for a null buffer happens first.
        SerializedString serializedString = new SerializedString("any-value");
        byte[] nullBuffer = null;
        int offset = 0;

        // Act: Attempt to append the string's UTF-8 representation to a null buffer.
        // The @Test(expected) annotation will handle the assertion.
        serializedString.appendQuotedUTF8(nullBuffer, offset);

        // Assert: The test will pass if a NullPointerException is thrown, as specified by
        // the @Test annotation. If no exception or a different one is thrown, the test will fail.
    }
}