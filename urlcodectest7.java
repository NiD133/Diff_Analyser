package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests for the URLCodec's handling of default character sets.
 */
public class URLCodecDefaultEncodingTest {

    /**
     * Tests that the {@link URLCodec#encode(String)} method correctly uses the
     * default charset provided in the constructor.
     * <p>
     * The test verifies this by comparing the output of the single-argument
     * {@code encode(String)} with the output of {@code encode(String, String)}
     * when the same charset is passed explicitly.
     * </p>
     */
    @Test
    void encodeShouldUseDefaultCharsetSpecifiedInConstructor() throws EncoderException, UnsupportedEncodingException {
        // Arrange: Define the input string, the charset, and instantiate the codec
        // with the chosen charset as its default.
        final String plainText = "Hello there!";
        final String charsetName = "UnicodeBig";
        final URLCodec urlCodec = new URLCodec(charsetName);

        // Act: Encode the string in two ways:
        // 1. Explicitly providing the charset.
        final String encodedWithExplicitCharset = urlCodec.encode(plainText, charsetName);
        // 2. Relying on the default charset set in the constructor.
        final String encodedWithDefaultCharset = urlCodec.encode(plainText);

        // Assert: The two encoded strings must be identical.
        assertEquals(encodedWithExplicitCharset, encodedWithDefaultCharset,
                "Encoding with the default charset should be equivalent to explicitly using that same charset.");
    }
}