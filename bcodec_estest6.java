package org.apache.commons.codec.net;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the BCodec class, focusing on byte-level encoding.
 */
public class BCodecTest {

    @Test
    public void doEncodingWithInputNotMultipleOfThreeShouldBePaddedAndEncodedCorrectly() {
        // Arrange
        // The BCodec uses Base64 encoding, which processes data in 3-byte chunks.
        // When the input length is not a multiple of 3, the data is padded.
        final BCodec bCodec = new BCodec(); // Charset is not used for byte[] encoding.
        final byte[] inputData = new byte[7]; // 7 bytes = 2 chunks of 3 bytes + 1 remaining byte.

        // Act
        final byte[] encodedData = bCodec.doEncoding(inputData);

        // Assert
        // The 1 remaining byte is padded, resulting in a final 4-byte encoded block.
        // The total expected length is (2 * 4 bytes) + 4 bytes = 12 bytes.
        final int expectedLength = 12;
        assertEquals(
                "The encoded output length should be correct for a padded input.",
                expectedLength,
                encodedData.length);

        // A more specific assertion also checks the content for correctness.
        // An input of 7 null bytes is encoded to "AAAAAAAAAA==" in Base64.
        final byte[] expectedContent = "AAAAAAAAAA==".getBytes(StandardCharsets.US_ASCII);
        assertArrayEquals(
                "The encoded content should match the expected Base64 output.",
                expectedContent,
                encodedData);
    }
}