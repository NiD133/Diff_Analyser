package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 *
 * Note: The original test class name "ByteUtilsTestTest5" was renamed for clarity.
 */
class ByteUtilsTest {

    @Test
    @DisplayName("fromLittleEndian(byte[], int, int) should throw IllegalArgumentException for a length greater than 8")
    void fromLittleEndianWithByteArrayShouldThrowExceptionForLengthTooBig() {
        // Arrange
        // The method should validate the length before accessing the buffer,
        // so an empty buffer is sufficient for this test.
        final byte[] buffer = ByteUtils.EMPTY_BYTE_ARRAY;
        final int offset = 0;
        // A long is represented by at most 8 bytes. A requested length of 9 is invalid.
        final int lengthGreaterThanMax = 9;

        // Act & Assert
        // Verify that an IllegalArgumentException is thrown.
        assertThrows(IllegalArgumentException.class, () ->
            fromLittleEndian(buffer, offset, lengthGreaterThanMax)
        );
    }
}