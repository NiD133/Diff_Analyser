package org.apache.commons.codec.net;

import org.junit.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link PercentCodec} class.
 */
public class PercentCodecTest {

    /**
     * Tests that the encode method returns the same byte array instance
     * when the input contains no characters that require encoding.
     * This verifies an important optimization where the input array is returned
     * directly if no modifications are necessary.
     */
    @Test
    public void encodeShouldReturnSameArrayInstanceWhenNoCharactersNeedEncoding() {
        // Arrange
        // Create a codec with an empty list of characters to always encode.
        // This means only non-US-ASCII characters and '%' should be encoded.
        final PercentCodec percentCodec = new PercentCodec(new byte[0], true);

        // Create an input byte array with "safe" US-ASCII characters that do not need encoding.
        final byte[] unencodedBytes = "abcdef-12345".getBytes(StandardCharsets.US_ASCII);

        // Act
        final byte[] encodedBytes = percentCodec.encode(unencodedBytes);

        // Assert
        // The codec should recognize that no encoding is needed and return the original array instance,
        // not a copy.
        assertSame("Expected the same array instance when no encoding is performed", unencodedBytes, encodedBytes);
    }
}