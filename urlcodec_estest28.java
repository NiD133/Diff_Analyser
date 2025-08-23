package org.apache.commons.codec.net;

import org.junit.Test;
import java.io.UnsupportedEncodingException;

/**
 * Unit tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that the decode method throws an UnsupportedEncodingException when
     * provided with an invalid or unsupported character set name.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeWithUnsupportedEncodingThrowsException() throws Exception {
        // Arrange
        final URLCodec urlCodec = new URLCodec();
        final String invalidCharset = "INVALID-ENCODING-NAME";
        final String input = "any string";

        // Act: Attempt to decode a string using an unsupported encoding.
        // Assert: The test expects an UnsupportedEncodingException to be thrown,
        // which is handled by the @Test(expected=...) annotation.
        urlCodec.decode(input, invalidCharset);
    }
}