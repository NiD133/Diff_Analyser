package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for illegal arguments in the {@link CircularByteBuffer#read(byte[], int, int)} method.
 */
@DisplayName("CircularByteBuffer.read(byte[], int, int)")
class CircularByteBufferReadIllegalArgumentTest {

    private CircularByteBuffer buffer;
    private byte[] destination;

    @BeforeEach
    void setUp() {
        buffer = new CircularByteBuffer();
        destination = new byte[10];
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when target offset is negative")
    void readShouldThrowExceptionForNegativeOffset() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            buffer.read(destination, -1, 1);
        });
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when length is negative")
    void readShouldThrowExceptionForNegativeLength() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            buffer.read(destination, 0, -1);
        });
    }

    @ParameterizedTest
    @CsvSource({
        "0, 11", // length is larger than the destination buffer
        "1, 10"  // offset + length exceeds the destination buffer bounds
    })
    @DisplayName("should throw IllegalArgumentException when write would exceed target buffer bounds")
    void readShouldThrowExceptionWhenTargetBufferIsTooSmall(final int offset, final int length) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            buffer.read(destination, offset, length);
        });
    }
}