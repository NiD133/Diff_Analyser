package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import org.apache.commons.compress.utils.ByteUtils.InputStreamByteSupplier;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(ByteUtils.ByteSupplier, int)} throws an
     * IllegalArgumentException if the requested length is greater than 8, which is the
     * size of a long.
     */
    @Test
    void fromLittleEndianWithSupplierShouldThrowForLengthGreaterThanEight() {
        // Arrange
        // The length check happens before any bytes are read, so an empty supplier is sufficient.
        final InputStreamByteSupplier supplier = new InputStreamByteSupplier(
                new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY));
        final int invalidLength = 9; // A long is 8 bytes, so 9 is an invalid length.

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(supplier, invalidLength),
                "Should throw an exception for a length greater than 8.");
    }
}