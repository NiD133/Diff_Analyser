package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Tests for {@link URLCodec}.
 */
public class URLCodecTest {

    /**
     * Tests that the encode method correctly percent-encodes a special character (semicolon)
     * while leaving alphanumeric characters untouched.
     */
    @Test
    public void encodeShouldPercentEncodeSpecialCharacters() throws UnsupportedEncodingException {
        // Arrange
        final URLCodec urlCodec = new URLCodec();
        final String originalString = "VoM;";
        final String expectedEncodedString = "VoM%3B";

        // Act
        final String actualEncodedString = urlCodec.encode(originalString, StandardCharsets.UTF_8.name());

        // Assert
        assertEquals("The semicolon should be percent-encoded.", expectedEncodedString, actualEncodedString);
    }
}