package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.compress.utils.ByteUtils.InputStreamByteSupplier;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 */
// The original class name 'ByteUtilsTestTest18' was likely auto-generated.
// A standard name like 'ByteUtilsTest' is more appropriate.
class ByteUtilsTest {

    /**
     * Tests that fromLittleEndian correctly reads a 4-byte value from a ByteSupplier
     * that represents an unsigned integer value. The chosen value (0x80040302L) is
     * larger than Integer.MAX_VALUE, ensuring the conversion to a long is handled correctly.
     */
    @Test
    void fromLittleEndianWithSupplierReadsUnsigned32BitValue() throws IOException {
        // Arrange
        // The little-endian byte representation of the number 0x80040302.
        final byte[] littleEndianBytes = {2, 3, 4, (byte) 0x80};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(littleEndianBytes);
        final InputStreamByteSupplier supplier = new InputStreamByteSupplier(inputStream);

        // The expected value is 2,147,746,562, which is 0x80040302 in hexadecimal.
        // Using hex makes the relationship to the input bytes more apparent.
        final long expectedValue = 0x80040302L;
        final int bytesToRead = 4;

        // Act
        final long actualValue = fromLittleEndian(supplier, bytesToRead);

        // Assert
        assertEquals(expectedValue, actualValue,
            "Should correctly decode a 4-byte little-endian value representing an unsigned integer.");
    }
}