package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 */
class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(DataInput, int)} throws an
     * IllegalArgumentException when the requested length is greater than Long.BYTES (8).
     * The method should validate the length before attempting to read from the input.
     */
    @Test
    void fromLittleEndianWithDataInputShouldThrowExceptionForLengthGreaterThan8() {
        // Arrange
        // The input stream can be empty as the length check should precede any read operations.
        final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY));
        final int invalidLength = Long.BYTES + 1; // The maximum allowed length is 8.

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(dataInput, invalidLength));
    }
}