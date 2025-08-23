package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.UnsupportedCharsetException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the BCodec class, focusing on its constructor behavior with invalid inputs.
 */
class BCodecTest {

    @Test
    @DisplayName("Constructor should throw UnsupportedCharsetException for an invalid charset name")
    void constructorWithInvalidCharsetNameShouldThrowException() {
        // Arrange: Define a charset name that is known to be invalid.
        final String invalidCharsetName = "INVALID-CHARSET-NAME";

        // Act & Assert: Verify that instantiating BCodec with the invalid name
        // throws the expected exception.
        assertThrows(UnsupportedCharsetException.class, () -> new BCodec(invalidCharsetName));
    }
}