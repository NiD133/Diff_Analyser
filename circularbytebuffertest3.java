package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CircularByteBuffer}.
 */
class CircularByteBufferTest {

    @Test
    void addShouldThrowIllegalArgumentExceptionForNegativeLength() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] dummyData = new byte[0]; // Data content is irrelevant for this test.
        final int offset = 0;
        final int negativeLength = -1;

        // Act & Assert
        // Verify that calling add() with a negative length throws the expected exception.
        assertThrows(IllegalArgumentException.class, () -> {
            buffer.add(dummyData, offset, negativeLength);
        });
    }
}