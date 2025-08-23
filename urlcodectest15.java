package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link URLCodec} class, focusing on encoding and decoding behavior.
 */
class URLCodecTest {

    @Test
    @DisplayName("URLCodec should correctly encode and decode a string containing unsafe characters")
    void shouldCorrectlyEncodeAndDecodeUnsafeCharacters() throws EncoderException, DecoderException {
        // Arrange
        final URLCodec urlCodec = new URLCodec();

        // A string containing characters that are not part of the 'www-form-urlencoded' safe set.
        // These characters are expected to be percent-encoded.
        final String originalString = "~!@#$%^&()+{}\"\\;:`,/[]";
        final String expectedEncodedString = "%7E%21%40%23%24%25%5E%26%28%29%2B%7B%7D%22%5C%3B%3A%60%2C%2F%5B%5D";

        // Act: Encode the original string
        final String actualEncodedString = urlCodec.encode(originalString);

        // Assert: The encoded string must match the expected format
        assertEquals(expectedEncodedString, actualEncodedString,
            "Encoding of unsafe characters did not produce the expected result.");

        // Act: Decode the resulting string
        final String decodedString = urlCodec.decode(actualEncodedString);

        // Assert: The decoded string must match the original string (round-trip test)
        assertEquals(originalString, decodedString,
            "Decoding the encoded string should restore the original string.");
    }
}