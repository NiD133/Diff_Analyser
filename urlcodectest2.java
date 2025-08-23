package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for invalid decoding scenarios in {@link URLCodec}.
 */
class URLCodecTest {

    private URLCodec urlCodec;

    @BeforeEach
    void setUp() {
        this.urlCodec = new URLCodec();
    }

    @DisplayName("decode() should throw DecoderException for malformed percent-encoded strings")
    @ParameterizedTest(name = "Invalid input: \"{0}\" ({1})")
    @CsvSource({
        "'%',      'Incomplete escape sequence, missing two hex digits'",
        "'%A',     'Incomplete escape sequence, missing one hex digit'",
        "'%WW',    'Invalid non-hex character ''W'' in first position'",
        "'%0W',    'Invalid non-hex character ''W'' in second position'"
    })
    void decodeWithMalformedInputThrowsDecoderException(final String invalidInput, final String description) {
        // The 'description' parameter from CsvSource serves as inline documentation
        // for why each input is expected to fail.
        assertThrows(DecoderException.class, () -> {
            urlCodec.decode(invalidInput);
        });
    }
}