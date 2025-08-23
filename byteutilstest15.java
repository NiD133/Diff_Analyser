package org.apache.commons.compress.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.compress.utils.ByteUtils.InputStreamByteSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ByteUtils} class, focusing on the fromLittleEndian method
 * with a ByteSupplier.
 */
public class ByteUtilsTest {

    @Test
    @DisplayName("fromLittleEndian should correctly read a 3-byte value from a ByteSupplier")
    void fromLittleEndianWithSupplierReadsCorrectValue() throws IOException {
        // Arrange: Set up the input data and the expected result.
        // The input stream contains {2, 3, 4, 5}. We will read the first 3 bytes.
        final byte[] inputBytes = {2, 3, 4, 5};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        final InputStreamByteSupplier supplier = new InputStreamByteSupplier(inputStream);
        final int bytesToRead = 3;

        // In little-endian, the first byte is the least significant.
        // Reading 3 bytes {2, 3, 4} results in the number 0x040302.
        // This is equivalent to: 2 * (256^0) + 3 * (256^1) + 4 * (256^2) = 262914.
        final long expectedValue = 0x040302L;

        // Act: Call the method under test.
        final long actualValue = ByteUtils.fromLittleEndian(supplier, bytesToRead);

        // Assert: Verify that the actual result matches the expected value.
        assertEquals(expectedValue, actualValue, "The 3-byte little-endian value should be read correctly.");
    }
}