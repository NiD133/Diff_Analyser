package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Tests for {@link BCodec}.
 */
public class BCodecTest {

    /**
     * Tests that the encode method throws a NullPointerException when the provided
     * Charset is null. The underlying implementation requires a non-null Charset
     * to convert the input string into bytes for encoding.
     */
    @Test(expected = NullPointerException.class)
    public void encodeWithNullCharsetShouldThrowNullPointerException() throws EncoderException {
        // Arrange
        final BCodec bCodec = new BCodec();
        final String input = "any string";
        final Charset nullCharset = null;

        // Act: This call is expected to throw a NullPointerException.
        bCodec.encode(input, nullCharset);

        // Assert: The test passes if a NullPointerException is thrown.
    }
}