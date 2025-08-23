package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that the write(byte[], int, int) method correctly throws a
     * NullPointerException when the provided byte array buffer is null.
     */
    @Test
    public void write_shouldThrowNullPointerException_whenBufferIsNull() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] nullBuffer = null;
        int offset = 0;
        int length = 0;

        // Act & Assert
        // The call to write() is expected to fail fast with a NullPointerException
        // because the source buffer cannot be null.
        assertThrows(NullPointerException.class, () -> {
            builder.write(nullBuffer, offset, length);
        });
    }
}