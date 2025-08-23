package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the BCodec class.
 */
public class BCodecTest {

    /**
     * Tests that an RFC 1522 "B" encoded-word representing an empty string
     * is correctly decoded back into an empty string.
     *
     * <p>The input "=?UTF-8?B??=" follows the RFC 1522 format:</p>
     * <ul>
     *   <li>"=?UTF-8": The character set.</li>
     *   <li>"?B?": The 'B' (Base64) encoding scheme.</li>
     *   <li>"": The Base64 encoded content, which is empty.</li>
     *   <li>"?=": The closing delimiter.</li>
     * </ul>
     */
    @Test
    public void shouldDecodeEmptyEncodedWordToEmptyString() throws DecoderException {
        // Arrange
        BCodec codec = new BCodec(); // Uses UTF-8 by default, which is a sensible default.
        String encodedEmptyString = "=?UTF-8?B??=";

        // Act
        String decodedString = codec.decode(encodedEmptyString);

        // Assert
        assertEquals("Decoding an empty encoded word should result in an empty string.", "", decodedString);
    }
}