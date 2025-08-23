package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link SerializedString} class, focusing on invalid argument scenarios.
 */
public class SerializedStringTest {

    /**
     * Verifies that calling {@link SerializedString#appendUnquoted(char[], int)} with a negative
     * offset correctly throws an {@link ArrayIndexOutOfBoundsException}.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void appendUnquoted_whenGivenNegativeOffset_shouldThrowException() {
        // Arrange: Create a SerializedString and a destination buffer.
        // The offset is intentionally negative to trigger the expected exception.
        SerializedString serializedString = new SerializedString("any-string");
        char[] destinationBuffer = new char[10];
        int negativeOffset = -1;

        // Act: Attempt to append the string to the buffer at the invalid negative offset.
        // This call is expected to throw the exception.
        serializedString.appendUnquoted(destinationBuffer, negativeOffset);

        // Assert: The test framework verifies that an ArrayIndexOutOfBoundsException was thrown,
        // as specified by the @Test(expected=...) annotation. No further assertions are needed.
    }
}