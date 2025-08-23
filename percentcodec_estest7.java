package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

/**
 * Test suite for {@link PercentCodec}.
 */
public class PercentCodecTest {

    /**
     * Tests that the encoder correctly handles a custom set of "always encode" characters.
     * In this case, we configure the codec to always encode the null byte ('\0').
     */
    @Test(timeout = 4000)
    public void encodeShouldCorrectlyProcessCustomAlwaysEncodeChars() throws Exception {
        // Arrange
        // Define that the null byte should always be percent-encoded.
        final byte[] charsToAlwaysEncode = {0};
        final PercentCodec percentCodec = new PercentCodec(charsToAlwaysEncode, true);

        // The input contains six null bytes (which must be encoded) and one safe
        // character '?' (which should pass through unchanged).
        final byte[] inputData = {0, 0, 0, 0, 0, 0, '?'};

        // Act
        final byte[] encodedData = percentCodec.encode(inputData);

        // Assert
        // Each null byte (0) is encoded to "%00" (3 bytes).
        // The '?' character is a safe US-ASCII character and is not encoded.
        // Expected output: "%00%00%00%00%00%00?"
        final byte[] expectedData = "%00%00%00%00%00%00?".getBytes(StandardCharsets.US_ASCII);

        // A robust test verifies the content, not just the length.
        assertArrayEquals(expectedData, encodedData);

        // For completeness, we can also verify the length explicitly.
        // 6 encoded nulls * 3 bytes/char + 1 unencoded char = 19 bytes.
        assertEquals(19, encodedData.length);
    }
}