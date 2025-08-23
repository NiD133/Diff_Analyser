package org.apache.commons.codec.net;

import org.junit.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the {@link PercentCodec} class.
 */
public class PercentCodecTest {

    /**
     * Tests that the encode method correctly percent-encodes bytes that are
     * configured as "unsafe", while leaving other "safe" bytes unchanged.
     */
    @Test
    public void encodeShouldEncodeUnsafeBytesAndPassThroughSafeBytes() throws Exception {
        // Arrange
        // Define a set of characters that should always be percent-encoded.
        final byte[] unsafeBytes = {'A', 'B'};
        final PercentCodec percentCodec = new PercentCodec(unsafeBytes, false);

        // The input contains characters configured as unsafe ('A', 'B')
        // and one that is not ('C'), which should be treated as safe.
        final byte[] input = {'A', 'B', 'C'};

        // Act
        final byte[] encoded = percentCodec.encode(input);

        // Assert
        // 'A' (ASCII 65, hex 41) should be encoded to "%41".
        // 'B' (ASCII 66, hex 42) should be encoded to "%42".
        // 'C' is not in the unsafe set, so it should pass through unchanged.
        final byte[] expected = "%41%42C".getBytes(StandardCharsets.US_ASCII);
        assertArrayEquals(expected, encoded);
    }
}