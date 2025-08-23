package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertSame;

import java.nio.charset.StandardCharsets;

/**
 * Tests for the PercentCodec class.
 */
public class PercentCodecTest {

    /**
     * Tests that the encode() method returns the original byte array instance
     * when the input contains no characters that require encoding. This verifies
     * an important optimization that avoids unnecessary object allocation.
     */
    @Test
    public void encodeShouldReturnSameArrayInstanceForSafeBytes() throws Exception {
        // Arrange
        // Input contains only "safe" ASCII characters that do not need percent-encoding.
        final byte[] safeBytes = "abcdef12345".getBytes(StandardCharsets.US_ASCII);

        // A codec configured with no special characters to encode (null).
        // The '%' character is always considered unsafe internally.
        final PercentCodec percentCodec = new PercentCodec(null, true);

        // Act
        final byte[] encodedBytes = percentCodec.encode(safeBytes);

        // Assert
        // The returned array should be the exact same instance as the input,
        // not just an equal one, to confirm the optimization is active.
        assertSame("Expected the original array instance to be returned when no encoding is needed",
                   safeBytes, encodedBytes);
    }
}