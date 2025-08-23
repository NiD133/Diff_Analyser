package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

// The original class name and inheritance are preserved as the context is a single test file.
public class URLCodec_ESTestTest23 extends URLCodec_ESTest_scaffolding {

    /**
     * Tests that calling encode() on a URLCodec instance initialized with a null
     * charset throws a NullPointerException.
     * <p>
     * This behavior is expected because the underlying implementation attempts to get
     * the bytes of the input string using a null charset name, which results in an NPE.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void encodeWithNullCharsetThrowsNullPointerException() throws EncoderException {
        // Arrange: Create a URLCodec instance with a null charset.
        final URLCodec urlCodec = new URLCodec(null);
        final String stringToEncode = "test string";

        // Act & Assert: Calling encode should throw a NullPointerException.
        // The assertion is handled by the @Test(expected) annotation.
        urlCodec.encode(stringToEncode);
    }
}