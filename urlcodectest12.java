package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the static methods of {@link URLCodec}.
 */
class URLCodecStaticTest {

    @Test
    @DisplayName("The static encodeUrl method should use default safe characters when the provided BitSet is null")
    void staticEncodeUrlWithNullBitSetShouldUseDefaultEncoding() throws DecoderException {
        // Arrange
        final String plainText = "Hello there!";
        final byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
        final String expectedEncodedText = "Hello+there%21";

        // Act: Call the static encodeUrl method with a null BitSet, which should
        // trigger the default encoding behavior (e.g., space to '+', '!' to '%21').
        final byte[] encodedBytes = URLCodec.encodeUrl(null, plainTextBytes);
        final String actualEncodedText = new String(encodedBytes, StandardCharsets.UTF_8);

        // Assert: Verify that the encoding is correct.
        assertEquals(expectedEncodedText, actualEncodedText, "Encoding with a null BitSet should produce the default URL-encoded string.");

        // Arrange for decoding
        final URLCodec codec = new URLCodec();

        // Act & Assert: Verify that the encoded string can be decoded back to the original.
        final String decodedText = codec.decode(actualEncodedText);
        assertEquals(plainText, decodedText, "Decoding the result should return the original string.");
    }
}