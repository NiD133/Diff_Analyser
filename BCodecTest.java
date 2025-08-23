package org.apache.commons.codec.net;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for RFC 1522 "B" (Base64) encoded-word codec.
 * 
 * The tests favor small, focused cases with descriptive names and shared utilities/constants
 * to keep intent obvious and maintenance simple.
 */
@DisplayName("BCodec (RFC 1522 Base64 encoded-word)")
class BCodecTest {

    // Samples that require RFC 1522 encoded-word handling and contain invalid trailing bits for Base64.
    private static final String[] BASE64_IMPOSSIBLE_CASES = {
        "=?ASCII?B?ZE==?=",
        "=?ASCII?B?ZmC=?=",
        "=?ASCII?B?Zm9vYE==?=",
        "=?ASCII?B?Zm9vYmC=?=",
        "=?ASCII?B?AB==?="
    };

    // Handy constants to make assertions easier to understand at a glance.
    private static final String PLAIN_HELLO = "Hello there";
    private static final String ENCODED_HELLO = "=?UTF-8?B?SGVsbG8gdGhlcmU=?=";

    private static final String PLAIN_WHAT_NOT = "what not";
    private static final String ENCODED_WHAT_NOT = "=?UTF-8?B?d2hhdCBub3Q=?=";

    private static final int[] CODEPOINTS_SWISS_GERMAN = { 0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 };
    private static final int[] CODEPOINTS_RUSSIAN = { 0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 };

    private static final String ENCODED_RUSSIAN = "=?UTF-8?B?0JLRgdC10Lxf0L/RgNC40LLQtdGC?=";
    private static final String ENCODED_SWISS_GERMAN = "=?UTF-8?B?R3LDvGV6aV96w6Rtw6Q=?=";

    // Utility: build a String from Unicode code points.
    private static String fromCodePoints(final int[] codePoints) {
        return codePoints == null ? null : new String(codePoints, 0, codePoints.length);
    }

    // Convenience creators for different decoding policies.
    private static BCodec newLenient() {
        return new BCodec(UTF_8, CodecPolicy.LENIENT);
    }

    private static BCodec newStrict() {
        return new BCodec(UTF_8, CodecPolicy.STRICT);
    }

    @Nested
    @DisplayName("Decoding policy")
    class DecodingPolicyTests {

        @ParameterizedTest(name = "Lenient by default decodes: \"{0}\"")
        @ValueSource(strings = {
            "=?ASCII?B?ZE==?=", "=?ASCII?B?ZmC=?=", "=?ASCII?B?Zm9vYE==?=", "=?ASCII?B?Zm9vYmC=?=", "=?ASCII?B?AB==?="
        })
        void defaultIsLenient_andDoesNotThrow(final String sample) {
            final BCodec codec = new BCodec(); // Default constructor is lenient
            assertFalse(codec.isStrictDecoding());
            assertDoesNotThrow(() -> codec.decode(sample));
        }

        @ParameterizedTest(name = "Explicit LENIENT decodes: \"{0}\"")
        @ValueSource(strings = {
            "=?ASCII?B?ZE==?=", "=?ASCII?B?ZmC=?=", "=?ASCII?B?Zm9vYE==?=", "=?ASCII?B?Zm9vYmC=?=", "=?ASCII?B?AB==?="
        })
        void lenientDoesNotThrow(final String sample) {
            final BCodec codec = newLenient();
            assertFalse(codec.isStrictDecoding());
            assertDoesNotThrow(() -> codec.decode(sample));
        }

        @ParameterizedTest(name = "STRICT throws on: \"{0}\"")
        @ValueSource(strings = {
            "=?ASCII?B?ZE==?=", "=?ASCII?B?ZmC=?=", "=?ASCII?B?Zm9vYE==?=", "=?ASCII?B?Zm9vYmC=?=", "=?ASCII?B?AB==?="
        })
        void strictThrowsDecoderException(final String sample) {
            final BCodec codec = newStrict();
            assertTrue(codec.isStrictDecoding());
            assertThrows(DecoderException.class, () -> codec.decode(sample));
        }
    }

    @Test
    @DisplayName("Basic encode/decode of simple ASCII text")
    void encodeDecode_basicAscii() throws Exception {
        final BCodec codec = new BCodec();
        final String encoded = codec.encode(PLAIN_HELLO);
        assertEquals(ENCODED_HELLO, encoded);
        assertEquals(PLAIN_HELLO, codec.decode(encoded));
    }

    @Test
    @DisplayName("decode(Object) handles String, null, and rejects non-String")
    void decodeObject_behavior() throws Exception {
        final BCodec codec = new BCodec();

        final String asString = (String) codec.decode((Object) ENCODED_WHAT_NOT);
        assertEquals(PLAIN_WHAT_NOT, asString);

        assertNull(codec.decode((Object) null));
        assertThrows(DecoderException.class, () -> codec.decode(3.0));
    }

    @Test
    @DisplayName("decode(String) returns null on null input")
    void decode_nullString_returnsNull() throws Exception {
        final BCodec codec = new BCodec();
        assertNull(codec.decode((String) null));
    }

    @Test
    @DisplayName("encode/decode(String) return null on null input")
    void encodeDecode_nullString_returnsNull() throws Exception {
        final BCodec codec = new BCodec();
        assertNull(codec.encode((String) null));
        assertNull(codec.decode((String) null));
    }

    @Test
    @DisplayName("encode(Object) handles String, null, and rejects non-String")
    void encodeObject_behavior() throws Exception {
        final BCodec codec = new BCodec();

        final String encoded = (String) codec.encode((Object) PLAIN_WHAT_NOT);
        assertEquals(ENCODED_WHAT_NOT, encoded);

        assertNull(codec.encode((Object) null));
        assertThrows(EncoderException.class, () -> codec.encode(3.0));
    }

    @Test
    @DisplayName("encode(String, String) returns null when source is null (ignores charset)")
    void encode_withNullSource_returnsNullEvenWithCharset() throws Exception {
        final BCodec codec = new BCodec();
        assertNull(codec.encode((String) null, "charset"));
    }

    @Test
    @DisplayName("Constructor rejects invalid Charset name")
    void constructor_invalidCharset_throws() {
        assertThrows(UnsupportedCharsetException.class, () -> new BCodec("NONSENSE"));
    }

    @Test
    @DisplayName("Low-level doEncoding/doDecoding return null on null input")
    void lowLevel_nullInput_returnsNull() throws Exception {
        final BCodec codec = new BCodec();
        assertNull(codec.doDecoding(null));
        assertNull(codec.doEncoding(null));
    }

    @Test
    @DisplayName("UTF-8 round-trip and known encodings for non-ASCII samples")
    void utf8_roundTrip_andExpectedEncodings() throws Exception {
        final String russian = fromCodePoints(CODEPOINTS_RUSSIAN);
        final String swissGerman = fromCodePoints(CODEPOINTS_SWISS_GERMAN);

        final BCodec codec = new BCodec(StandardCharsets.UTF_8);

        // Known encodings
        assertEquals(ENCODED_RUSSIAN, codec.encode(russian));
        assertEquals(ENCODED_SWISS_GERMAN, codec.encode(swissGerman));

        // Round-trip
        assertEquals(russian, codec.decode(codec.encode(russian)));
        assertEquals(swissGerman, codec.decode(codec.encode(swissGerman)));
    }
}