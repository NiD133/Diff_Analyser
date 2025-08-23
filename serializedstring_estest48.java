package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link SerializedString} class, focusing on its exception-handling behavior.
 */
public class SerializedStringTest {

    /**
     * Verifies that calling {@link SerializedString#appendQuoted(char[], int)} with a null buffer
     * results in a {@link NullPointerException}.
     */
    @Test
    public void appendQuoted_withNullBuffer_shouldThrowNullPointerException() {
        // Arrange
        SerializedString serializedString = new SerializedString("Q");
        // The offset value is arbitrary, as the null check on the buffer should occur first.
        int irrelevantOffset = -932;

        // Act & Assert
        // Expect a NullPointerException when the destination buffer is null.
        assertThrows(NullPointerException.class, () -> {
            serializedString.appendQuoted(null, irrelevantOffset);
        });
    }
}