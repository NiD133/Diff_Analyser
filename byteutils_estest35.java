package org.apache.commons.compress.utils;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteUtils} class, focusing on exception handling.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(byte[], int, int)} throws an
     * {@link ArrayIndexOutOfBoundsException} if the combination of offset and length
     * would cause an access beyond the array's bounds.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void fromLittleEndianWithOffsetAndLengthShouldThrowExceptionWhenAccessIsOutOfBounds() {
        // Arrange: Create a byte array and define an offset and length that will
        // result in reading past the end of the array.
        // Array size is 10. Reading 8 bytes from offset 8 would require indices up to 15.
        byte[] buffer = new byte[10];
        int offset = 8;
        int length = 8;

        // Act & Assert: This call is expected to throw an ArrayIndexOutOfBoundsException,
        // which is declared by the @Test annotation.
        ByteUtils.fromLittleEndian(buffer, offset, length);
    }
}