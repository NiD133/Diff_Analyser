package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link Base16} class.
 */
public class Base16Test {

    /**
     * Tests that calling the encode method with a negative length, which is an invalid
     * argument, is handled gracefully by returning an empty byte array instead of
     * throwing an exception.
     */
    @Test
    public void encodeWithNegativeLengthShouldReturnEmptyArray() {
        // Arrange
        final Base16 base16 = new Base16();
        final byte[] sourceData = new byte[]{ 0x01, 0x02, 0x03 }; // Arbitrary non-empty data
        final int offset = 0;
        final int negativeLength = -1; // A representative negative value for length

        // Act
        // Note: The original test calls a method with the signature encode(byte[], int, int).
        // We assume this method exists for the purpose of this test.
        final byte[] encodedBytes = base16.encode(sourceData, offset, negativeLength);

        // Assert
        assertNotNull("The result of encoding should not be null.", encodedBytes);
        assertArrayEquals("Encoding with a negative length should produce an empty byte array.",
                          new byte[0], encodedBytes);
    }
}