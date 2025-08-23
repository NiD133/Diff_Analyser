package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link CircularByteBuffer} class, focusing on edge cases for the peek() method.
 */
class CircularByteBufferTest {

    /**
     * Tests that {@link CircularByteBuffer#peek(byte[], int, int)} returns false
     * when the requested length to check is greater than the size of the provided
     * source byte array.
     *
     * This tests an argument validation case. The method should handle the invalid
     * length gracefully by returning false, rather than throwing an
     * ArrayIndexOutOfBoundsException.
     */
    @Test
    void peekShouldReturnFalseWhenLengthIsGreaterThanSourceArraySize() {
        // Arrange
        // An empty buffer is used, but the key aspect of this test is the arguments to peek().
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] sourceData = {1, 3, 5, 7, 9};
        final int offset = 0;
        // The length to peek is intentionally larger than the sourceData array.
        final int lengthToPeek = sourceData.length + 1; // 6

        // Act
        final boolean result = buffer.peek(sourceData, offset, lengthToPeek);

        // Assert
        assertFalse(result, "peek() should return false when the requested length is out of bounds for the source array.");
    }
}