package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that attempting to decode a string with a codec initialized with a null
     * charset throws a NullPointerException.
     * <p>
     * The underlying implementation eventually calls {@code new String(bytes, charset)},
     * which does not permit a null charset.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void decodeStringWithNullCharsetThrowsNullPointerException() throws DecoderException {
        // Arrange: Create a codec instance with a null charset.
        QuotedPrintableCodec codecWithNullCharset = new QuotedPrintableCodec((Charset) null);
        String irrelevantInput = "test string";

        // Act & Assert: Decoding any string should throw a NullPointerException.
        codecWithNullCharset.decode(irrelevantInput);
    }
}