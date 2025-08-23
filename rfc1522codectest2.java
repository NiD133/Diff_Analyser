package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the common functionality of the abstract {@link RFC1522Codec} class,
 * focusing on its handling of null inputs.
 */
@DisplayName("RFC1522Codec")
class RFC1522CodecTest {

    /**
     * A concrete test implementation of the abstract {@link RFC1522Codec}.
     * This stub allows testing the non-abstract methods of the base class.
     * The encoding/decoding methods are identity operations (pass-through),
     * as their specific logic is not under test here.
     */
    private static class RFC1522TestCodec extends RFC1522Codec {

        RFC1522TestCodec() {
            super(StandardCharsets.UTF_8);
        }

        @Override
        protected byte[] doDecoding(final byte[] bytes) {
            // No-op for this test, returns bytes as is.
            return bytes;
        }

        @Override
        protected byte[] doEncoding(final byte[] bytes) {
            // No-op for this test, returns bytes as is.
            return bytes;
        }

        @Override
        protected String getEncoding() {
            // "T" for "Test"
            return "T";
        }
    }

    @Test
    @DisplayName("decodeText should return null when input is null")
    void decodeTextWithNullInputShouldReturnNull() throws DecoderException, UnsupportedEncodingException {
        // Arrange
        final RFC1522Codec codec = new RFC1522TestCodec();

        // Act
        final String decodedText = codec.decodeText(null);

        // Assert
        assertNull(decodedText, "Expected decodeText(null) to return null");
    }

    @Test
    @DisplayName("encodeText should return null when input is null")
    void encodeTextWithNullInputShouldReturnNull() throws EncoderException {
        // Arrange
        final RFC1522Codec codec = new RFC1522TestCodec();

        // Act
        final String encodedText = codec.encodeText(null, CharEncoding.UTF_8);

        // Assert
        assertNull(encodedText, "Expected encodeText(null, ...) to return null");
    }
}