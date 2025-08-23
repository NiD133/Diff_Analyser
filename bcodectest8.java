package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class BCodecTestTest8 {

    private static final String[] BASE64_IMPOSSIBLE_CASES = { // Require the RFC 1522 "encoded-word" header
    "=?ASCII?B?ZE==?=", "=?ASCII?B?ZmC=?=", "=?ASCII?B?Zm9vYE==?=", "=?ASCII?B?Zm9vYmC=?=", "=?ASCII?B?AB==?=" };

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

    @Test
    void testEncodeObjects() throws Exception {
        final BCodec bcodec = new BCodec();
        final String plain = "what not";
        final String encoded = (String) bcodec.encode((Object) plain);
        assertEquals("=?UTF-8?B?d2hhdCBub3Q=?=", encoded, "Basic B encoding test");
        final Object result = bcodec.encode((Object) null);
        assertNull(result, "Encoding a null Object should return null");
        assertThrows(EncoderException.class, () -> bcodec.encode(Double.valueOf(3.0d)), "Trying to url encode a Double object should cause an exception.");
    }
}
