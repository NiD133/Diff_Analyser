package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the {@link RFC1522Codec} class, using the concrete {@link BCodec} implementation.
 */
public class BCodecTest {

    /**
     * Tests that decoding an RFC 1522 encoded-word with an empty payload
     * correctly results in an empty string.
     *
     * <p>The input "=?UTF-8?B??=" follows the RFC 1522 format:
     * {@code =?charset?encoding?encoded-text?=}. In this case, the encoded-text
     * part is empty, which should decode to an empty string.
     * </p>
     */
    @Test
    public void decodeTextWithEmptyPayloadShouldReturnEmptyString() throws Exception {
        // Arrange
        final BCodec codec = new BCodec();
        final String encodedEmptyString = "=?UTF-8?B??=";

        // Act
        final String decodedString = codec.decodeText(encodedEmptyString);

        // Assert
        assertEquals("Decoding an encoded word with an empty payload should result in an empty string.",
                     "", decodedString);
    }
}