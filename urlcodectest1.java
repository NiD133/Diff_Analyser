package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link URLCodec}.
 */
class URLCodecTest {

    @Test
    void shouldEncodeAndDecodeBasicString() throws EncoderException, DecoderException {
        // Arrange
        final URLCodec codec = new URLCodec();
        final String plainText = "Hello there!";
        final String expectedEncodedText = "Hello+there%21";

        // Act: Encode the plain text
        final String actualEncodedText = codec.encode(plainText);

        // Assert: Verify the encoded text is correct
        assertEquals(expectedEncodedText, actualEncodedText);

        // Act: Decode the encoded text to complete the round-trip
        final String decodedText = codec.decode(actualEncodedText);

        // Assert: Verify the decoded text matches the original plain text
        assertEquals(plainText, decodedText);
    }
}