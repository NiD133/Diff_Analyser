package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the QuotedPrintableCodec's handling of null String inputs.
 */
@DisplayName("QuotedPrintableCodec Null Handling")
class QuotedPrintableCodecTest {

    private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    @DisplayName("Encoding a null String should return null")
    void encodeNullStringShouldReturnNull() throws EncoderException {
        // The cast to (String) is necessary to resolve method ambiguity between
        // encode(String) and encode(byte[]).
        final String encodedResult = codec.encode((String) null);

        assertNull(encodedResult);
    }

    @Test
    @DisplayName("Decoding a null String should return null")
    void decodeNullStringShouldReturnNull() throws DecoderException {
        final String decodedResult = codec.decode((String) null);

        assertNull(decodedResult);
    }
}