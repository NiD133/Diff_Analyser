package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the BCodec class, focusing on byte-level encoding and decoding.
 */
public class BCodecTest {

    /**
     * Tests that decoding invalid Base64 data, followed by re-encoding the result,
     * produces an empty byte array. The default BCodec is lenient and should not throw
     * an exception for invalid input.
     */
    @Test
    public void decodeInvalidDataThenEncodeShouldProduceEmptyArray() {
        // Arrange
        // A byte array of zeros is not valid Base64 data.
        final byte[] invalidBase64Input = new byte[6];
        final BCodec bCodec = new BCodec(); // Uses lenient decoding by default.

        // Act
        // In lenient mode, decoding invalid data results in an empty array.
        final byte[] decodedBytes = bCodec.doDecoding(invalidBase64Input);
        // Re-encoding the (empty) result.
        final byte[] reEncodedBytes = bCodec.doEncoding(decodedBytes);

        // Assert
        final byte[] expectedEmptyArray = new byte[0];
        assertArrayEquals("Decoding invalid Base64 should yield an empty byte array", expectedEmptyArray, decodedBytes);
        assertArrayEquals("Encoding an empty byte array should also yield an empty byte array", expectedEmptyArray, reEncodedBytes);
    }
}