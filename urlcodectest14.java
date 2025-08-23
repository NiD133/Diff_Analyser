package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link URLCodec} focusing on the handling of "safe" characters
 * that do not require URL encoding.
 */
// Renamed from URLCodecTestTest14 for clarity and to follow standard naming conventions.
public class URLCodecTest {

    // According to RFC 3986, "unreserved" characters (alphanumerics and '-', '_', '.', '*')
    // do not need to be percent-encoded. These are considered safe.
    private static final String SAFE_CHARS_STRING = "abc123_-.*";

    private URLCodec urlCodec;

    @BeforeEach
    void setUp() {
        urlCodec = new URLCodec();
    }

    @Test
    @DisplayName("Encoding a string containing only safe characters should not alter the string")
    void encodingSafeCharactersShouldReturnOriginalString() throws EncoderException {
        // Arrange: The SAFE_CHARS_STRING constant and the urlCodec instance are prepared.

        // Act: Encode the string of safe characters.
        final String encodedString = urlCodec.encode(SAFE_CHARS_STRING);

        // Assert: The encoded string should be identical to the original.
        assertEquals(SAFE_CHARS_STRING, encodedString);
    }

    @Test
    @DisplayName("Decoding a string containing only safe characters should not alter the string")
    void decodingSafeCharactersShouldReturnOriginalString() throws DecoderException {
        // Arrange: The SAFE_CHARS_STRING constant and the urlCodec instance are prepared.

        // Act: Decode the string of safe characters.
        final String decodedString = urlCodec.decode(SAFE_CHARS_STRING);

        // Assert: The decoded string should be identical to the original.
        assertEquals(SAFE_CHARS_STRING, decodedString);
    }
}