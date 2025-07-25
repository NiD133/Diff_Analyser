package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the BCodec class, which handles Base64 encoding and decoding
 * with support for different character sets and decoding policies.
 */
class BCodecTest {

    // Test data for invalid Base64 encoded strings
    private static final String[] INVALID_BASE64_STRINGS = {
        "=?ASCII?B?ZE==?=",
        "=?ASCII?B?ZmC=?=",
        "=?ASCII?B?Zm9vYE==?=",
        "=?ASCII?B?Zm9vYmC=?=",
        "=?ASCII?B?AB==?="
    };

    // Unicode character arrays for testing
    private static final int[] SWISS_GERMAN_UNICODE = { 0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 };
    private static final int[] RUSSIAN_UNICODE = { 0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 };

    /**
     * Helper method to construct a string from an array of Unicode code points.
     */
    private String constructStringFromUnicode(final int[] unicodeChars) {
        final StringBuilder buffer = new StringBuilder();
        if (unicodeChars != null) {
            for (final int unicodeChar : unicodeChars) {
                buffer.append((char) unicodeChar);
            }
        }
        return buffer.toString();
    }

    @Test
    void testDecodeInvalidBase64StringsWithDefaultPolicy() throws DecoderException {
        final BCodec codec = new BCodec();
        assertFalse(codec.isStrictDecoding(), "Default decoding policy should be lenient");
        for (final String invalidString : INVALID_BASE64_STRINGS) {
            codec.decode(invalidString);
        }
    }

    @Test
    void testDecodeInvalidBase64StringsWithLenientPolicy() throws DecoderException {
        final BCodec codec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.LENIENT);
        assertFalse(codec.isStrictDecoding(), "Decoding policy should be lenient");
        for (final String invalidString : INVALID_BASE64_STRINGS) {
            codec.decode(invalidString);
        }
    }

    @Test
    void testDecodeInvalidBase64StringsWithStrictPolicy() {
        final BCodec codec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.STRICT);
        assertTrue(codec.isStrictDecoding(), "Decoding policy should be strict");
        for (final String invalidString : INVALID_BASE64_STRINGS) {
            assertThrows(DecoderException.class, () -> codec.decode(invalidString));
        }
    }

    @Test
    void testBasicEncodeDecode() throws Exception {
        final BCodec codec = new BCodec();
        final String plainText = "Hello there";
        final String encodedText = codec.encode(plainText);
        assertEquals("=?UTF-8?B?SGVsbG8gdGhlcmU=?=", encodedText, "Encoded text should match expected Base64 format");
        assertEquals(plainText, codec.decode(encodedText), "Decoded text should match original plain text");
    }

    @Test
    void testDecodeObjects() throws Exception {
        final BCodec codec = new BCodec();
        final String encodedText = "=?UTF-8?B?d2hhdCBub3Q=?=";
        final String decodedText = (String) codec.decode((Object) encodedText);
        assertEquals("what not", decodedText, "Decoded text should match expected value");
        assertNull(codec.decode((Object) null), "Decoding a null Object should return null");
        assertThrows(DecoderException.class, () -> codec.decode(Double.valueOf(3.0d)), "Decoding a non-string object should throw DecoderException");
    }

    @Test
    void testDecodeStringWithNull() throws Exception {
        final BCodec codec = new BCodec();
        assertNull(codec.decode((String) null), "Decoding a null string should return null");
    }

    @Test
    void testEncodeDecodeNull() throws Exception {
        final BCodec codec = new BCodec();
        assertNull(codec.encode((String) null), "Encoding a null string should return null");
        assertNull(codec.decode((String) null), "Decoding a null string should return null");
    }

    @Test
    void testEncodeObjects() throws Exception {
        final BCodec codec = new BCodec();
        final String plainText = "what not";
        final String encodedText = (String) codec.encode((Object) plainText);
        assertEquals("=?UTF-8?B?d2hhdCBub3Q=?=", encodedText, "Encoded text should match expected Base64 format");
        assertNull(codec.encode((Object) null), "Encoding a null Object should return null");
        assertThrows(EncoderException.class, () -> codec.encode(Double.valueOf(3.0d)), "Encoding a non-string object should throw EncoderException");
    }

    @Test
    void testEncodeStringWithNull() throws Exception {
        final BCodec codec = new BCodec();
        assertNull(codec.encode((String) null, "charset"), "Encoding a null string with charset should return null");
    }

    @Test
    void testInvalidEncoding() {
        assertThrows(UnsupportedCharsetException.class, () -> new BCodec("NONSENSE"), "Creating BCodec with invalid charset should throw UnsupportedCharsetException");
    }

    @Test
    void testNullInput() throws Exception {
        final BCodec codec = new BCodec();
        assertNull(codec.doDecoding(null), "Decoding null input should return null");
        assertNull(codec.doEncoding(null), "Encoding null input should return null");
    }

    @Test
    void testUTF8RoundTrip() throws Exception {
        final String russianText = constructStringFromUnicode(RUSSIAN_UNICODE);
        final String swissGermanText = constructStringFromUnicode(SWISS_GERMAN_UNICODE);
        final BCodec codec = new BCodec(CharEncoding.UTF_8);

        assertEquals("=?UTF-8?B?0JLRgdC10Lxf0L/RgNC40LLQtdGC?=", codec.encode(russianText), "Encoded Russian text should match expected Base64 format");
        assertEquals("=?UTF-8?B?R3LDvGV6aV96w6Rtw6Q=?=", codec.encode(swissGermanText), "Encoded Swiss German text should match expected Base64 format");

        assertEquals(russianText, codec.decode(codec.encode(russianText)), "Decoded Russian text should match original");
        assertEquals(swissGermanText, codec.decode(codec.encode(swissGermanText)), "Decoded Swiss German text should match original");
    }
}