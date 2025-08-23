package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link URLCodec} focusing on its behavior with invalid constructor arguments.
 */
public class URLCodecTest {

    private static final String INVALID_CHARSET_NAME = "NONSENSE";
    private static final String TEST_STRING = "Hello there!";

    private URLCodec urlCodecWithInvalidCharset;

    @BeforeEach
    void setUp() {
        // Arrange: Create a URLCodec instance with a non-existent charset.
        // This setup is shared by both tests.
        urlCodecWithInvalidCharset = new URLCodec(INVALID_CHARSET_NAME);
    }

    @Test
    void encodeWithInvalidCharsetShouldThrowEncoderException() {
        // Act & Assert: An EncoderException is expected because the charset provided
        // during construction is not supported.
        assertThrows(EncoderException.class, () -> {
            urlCodecWithInvalidCharset.encode(TEST_STRING);
        });
    }

    @Test
    void decodeWithInvalidCharsetShouldThrowDecoderException() {
        // Act & Assert: A DecoderException is expected because the charset provided
        // during construction is not supported. The content of the string being
        // decoded is irrelevant, as the exception occurs when creating the
        // final string from the decoded bytes.
        assertThrows(DecoderException.class, () -> {
            urlCodecWithInvalidCharset.decode(TEST_STRING);
        });
    }
}