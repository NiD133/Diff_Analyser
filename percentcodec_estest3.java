package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

// The original test class name and inheritance are preserved.
public class PercentCodec_ESTestTest3 extends PercentCodec_ESTest_scaffolding {

    /**
     * Tests that the PercentCodec can correctly encode and then decode a byte array
     * containing both non-ASCII characters and characters specified to be "always encoded".
     * This verifies that the encode-decode process is a lossless roundtrip.
     */
    @Test
    public void roundtripShouldPreserveNonAsciiAndAlwaysEncodeBytes() throws Exception {
        // --- Arrange ---

        // Configure the codec to always encode the null byte ('\0').
        // The constructor adds each unique byte from the input array to a set of
        // characters that must always be encoded.
        byte[] alwaysEncodeChars = {0x00};
        PercentCodec codec = new PercentCodec(alwaysEncodeChars, true);

        // Create input data containing a non-ASCII byte and several null bytes,
        // which are configured above to be always encoded.
        final byte[] originalBytes = {
            (byte) -19, // A non-ASCII byte (0xED) that will be percent-encoded.
            0, 0, 0, 0, 0, 0, 0, 0 // 8 null bytes that must also be percent-encoded.
        };

        // --- Act ---

        byte[] encodedBytes = codec.encode(originalBytes);
        byte[] decodedBytes = codec.decode(encodedBytes);

        // --- Assert ---

        // Verify the length of the encoded data.
        // 1 non-ASCII byte is encoded to 3 bytes (e.g., %ED).
        // 8 null bytes are each encoded to 3 bytes (%00).
        // Expected length = (1 * 3) + (8 * 3) = 3 + 24 = 27.
        assertEquals("Encoded data length is incorrect.", 27, encodedBytes.length);

        // The primary goal is to ensure the decoded data is identical to the original.
        assertArrayEquals("Decoded data should match original data.", originalBytes, decodedBytes);
    }
}