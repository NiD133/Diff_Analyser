package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for the QuotedPrintableCodec class.
 */
class QuotedPrintableCodecTest {

    /**
     * Tests that the {@link QuotedPrintableCodec#encode(String)} method correctly uses the
     * default charset provided in the constructor.
     * <p>
     * This is verified by comparing its output to the result of the
     * {@link QuotedPrintableCodec#encode(String, String)} method, where the same charset
     * is passed explicitly.
     * </p>
     */
    @Test
    void encodeShouldUseDefaultCharsetSpecifiedInConstructor() {
        // Arrange
        final String plainText = "Hello there!";
        final String charsetName = StandardCharsets.UTF_16BE.name(); // "UnicodeBig"

        // Instantiate the codec with a specific default charset.
        final QuotedPrintableCodec codec = new QuotedPrintableCodec(charsetName);

        // Act
        // 1. Encode the string using the overload that relies on the codec's default charset.
        final String encodedWithDefaultCharset = codec.encode(plainText);

        // 2. Encode the same string, but explicitly provide the charset name.
        final String encodedWithExplicitCharset = codec.encode(plainText, charsetName);

        // Assert
        // The results must be identical, confirming that the constructor correctly set the default charset.
        assertEquals(encodedWithExplicitCharset, encodedWithDefaultCharset);
    }
}