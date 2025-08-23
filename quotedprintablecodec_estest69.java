package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the generic {@code encode(Object)} method correctly encodes a string
     * ending with a space when the codec is in "strict" mode.
     * <p>
     * According to RFC 1521, in strict quoted-printable encoding, a space character
     * at the end of a line must be encoded to prevent it from being stripped by
     * mail transport systems. The space character (ASCII 32) is encoded as "=20".
     */
    @Test
    public void encodeObjectShouldEncodeTrailingSpaceInStrictMode() throws EncoderException {
        // Arrange
        final String inputString = "Invalid URL encoding: not a valid digit (radix 16): ";
        final String expectedEncodedString = "Invalid URL encoding: not a valid digit (radix 16):=20";
        
        // The 'true' argument enables strict encoding as per RFC 1521.
        final QuotedPrintableCodec strictCodec = new QuotedPrintableCodec(true);

        // Act
        final Object result = strictCodec.encode((Object) inputString);

        // Assert
        assertEquals(expectedEncodedString, result);
    }
}