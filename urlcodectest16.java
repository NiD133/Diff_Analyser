package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class URLCodecTestTest16 {

    static final int[] SWISS_GERMAN_STUFF_UNICODE = { 0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 };

    static final int[] RUSSIAN_STUFF_UNICODE = { 0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 };

    private String constructString(final int[] unicodeChars) {
        final StringBuilder buffer = new StringBuilder();
        if (unicodeChars != null) {
            for (final int unicodeChar : unicodeChars) {
                buffer.append((char) unicodeChar);
            }
        }
        return buffer.toString();
    }

    private void validateState(final URLCodec urlCodec) {
        // no tests for now.
    }

    @Test
    void testUTF8RoundTrip() throws Exception {
        final String ru_msg = constructString(RUSSIAN_STUFF_UNICODE);
        final String ch_msg = constructString(SWISS_GERMAN_STUFF_UNICODE);
        final URLCodec urlCodec = new URLCodec();
        validateState(urlCodec);
        assertEquals("%D0%92%D1%81%D0%B5%D0%BC_%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82", urlCodec.encode(ru_msg, CharEncoding.UTF_8));
        assertEquals("Gr%C3%BCezi_z%C3%A4m%C3%A4", urlCodec.encode(ch_msg, CharEncoding.UTF_8));
        assertEquals(ru_msg, urlCodec.decode(urlCodec.encode(ru_msg, CharEncoding.UTF_8), CharEncoding.UTF_8));
        assertEquals(ch_msg, urlCodec.decode(urlCodec.encode(ch_msg, CharEncoding.UTF_8), CharEncoding.UTF_8));
        validateState(urlCodec);
    }
}
