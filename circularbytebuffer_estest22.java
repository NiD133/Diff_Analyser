package org.apache.commons.io.input.buffer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link CircularByteBuffer} to ensure its methods behave as expected,
 * with a focus on clear, understandable test cases.
 */
public class CircularByteBufferTest {

    /**
     * Tests that the {@code read(byte[], int, int)} method correctly throws an
     * {@link IllegalArgumentException} when the specified length is too large
     * for the provided target buffer.
     */
    @Test
    void readThrowsIllegalArgumentExceptionWhenTargetBufferIsTooSmall() {
        // Arrange: Create a circular buffer. Its internal state is not relevant for this test.
        final CircularByteBuffer buffer = new CircularByteBuffer();

        // Arrange: Define a target buffer and a read length that exceeds its capacity.
        final byte[] targetBuffer = new byte[10];
        final int offset = 0;
        final int lengthToRead = targetBuffer.length + 1; // Intentionally one byte too many

        // Act & Assert: Verify that the read operation throws the expected exception.
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            buffer.read(targetBuffer, offset, lengthToRead);
        });

        // Assert: Verify the exception message is informative.
        // Based on the original test, the expected message format is "Illegal length: <length>".
        final String expectedMessage = "Illegal length: " + lengthToRead;
        assertEquals(expectedMessage, exception.getMessage());
    }
}