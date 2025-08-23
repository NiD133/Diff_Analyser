package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on edge cases
 * for its write operations.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that {@link ByteArrayBuilder#write(byte[], int, int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when the specified offset and length
     * would cause an access outside the bounds of the source array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void write_whenSourceAccessIsOutOfBounds_shouldThrowException() {
        // Arrange: Create a builder and a source byte array.
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] sourceData = new byte[500];

        // Act & Assert:
        // Attempt to write 500 bytes starting from an offset of 500.
        // This is invalid because it would require reading from indices 500 through 999
        // of sourceData, which only has valid indices from 0 to 499.
        // The write method is expected to detect this invalid access and throw.
        builder.write(sourceData, 500, 500);
    }
}