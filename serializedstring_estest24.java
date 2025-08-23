package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Contains tests for the {@link SerializedString} class.
 * This class focuses on verifying the behavior of its methods under various conditions.
 */
public class SerializedStringTest {

    /**
     * Verifies that appendUnquoted() throws a NullPointerException when the
     * provided character buffer is null.
     *
     * The method should perform a null check on the buffer before attempting any
     * other operations, such as validating the offset.
     */
    @Test(expected = NullPointerException.class)
    public void appendUnquoted_withNullBuffer_shouldThrowNullPointerException() {
        // Arrange: Create an instance of SerializedString. The content is irrelevant for this test.
        SerializedString serializedString = new SerializedString("test-string");
        char[] nullBuffer = null;
        int offset = 0; // The offset value does not matter, as the null check should occur first.

        // Act: Call the method with a null buffer.
        // This action is expected to throw a NullPointerException.
        serializedString.appendUnquoted(nullBuffer, offset);

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // by the @Test(expected = ...) annotation. No further assertions are needed.
    }
}