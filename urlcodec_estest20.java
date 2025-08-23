package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that decoding an object which is a simple string (containing no
     * URL-encoded characters) returns the original string.
     */
    @Test
    public void decodeObjectWithUnencodedStringShouldReturnOriginalString() throws DecoderException {
        // Arrange
        URLCodec urlCodec = new URLCodec();
        String inputString = "UTF-8";

        // Act
        // The decode(Object) method should delegate to decode(String) for String inputs.
        Object result = urlCodec.decode((Object) inputString);

        // Assert
        // Since the input string contains no encoded characters ('%' or '+'),
        // the decoded result should be identical to the input.
        assertEquals(inputString, result);
    }
}