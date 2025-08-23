package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for the QuotedPrintableCodec class, focusing on decoding invalid inputs.
 */
@DisplayName("QuotedPrintableCodec")
class QuotedPrintableCodecTest {

    private QuotedPrintableCodec codec;

    @BeforeEach
    void setUp() {
        // The codec is stateless, so a single instance can be reused for all tests.
        codec = new QuotedPrintableCodec();
    }

    @DisplayName("decode() should throw DecoderException for malformed sequences")
    @ParameterizedTest(name = "Input: \"{0}\"")
    @ValueSource(strings = {
        "=",   // An escape character must be followed by two hex digits.
        "=A",  // An escape sequence with only one hex digit is incomplete.
        "=WW"  // An escape sequence contains invalid non-hex characters.
    })
    void decodeWithMalformedInputShouldThrowException(final String invalidInput) {
        // The Quoted-Printable specification (RFC 1521) requires that an '=' escape
        // character be followed by two hexadecimal digits. These test cases violate that rule.
        assertThrows(DecoderException.class, () -> codec.decode(invalidInput));
    }
}