package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.UnsupportedCharsetException;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link QuotedPrintableCodec} class.
 */
class QuotedPrintableCodecTest {

    /**
     * Tests that the constructor throws an UnsupportedCharsetException when an invalid
     * charset name is provided. This ensures the codec cannot be instantiated with a
     * non-existent character encoding.
     */
    @Test
    void constructorWithInvalidCharsetNameShouldThrowException() {
        final String invalidCharsetName = "NONSENSE";

        // The constructor that accepts a charset name is expected to throw
        // an UnsupportedCharsetException if the charset is not supported.
        assertThrows(UnsupportedCharsetException.class,
            () -> new QuotedPrintableCodec(invalidCharsetName),
            "Constructor should fail for an invalid charset name.");
    }
}