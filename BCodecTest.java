package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite for BCodec, which handles Base64 encoding and decoding with specified charsets.
 */
class BCodecTest {

    private static final String[] BASE64_IMPOSSIBLE_CASES = {
        "=?ASCII?B?ZE==?=",
        "=?ASCII?B?ZmC=?=",
        "=?ASCII?B?Zm9vYE==?=",
        "=?ASCII?B?Zm9vYmC=?=",
        "=?ASCII?B?AB==?="
    };

    private static final int[] SWISS_GERMAN_UNICODE = { 0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 };
    private static final int[] RUSSIAN_UNICODE = { 0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 };

    private BCodec bcodec;

    @BeforeEach
    void setUp() {
        bcodec = new BCodec();
    }

    private String convertUnicodeToString(final int[] unicodeChars) {
        StringBuilder buffer = new StringBuilder();
        for (int unicodeChar : unicodeChars) {
            buffer.append((char) unicodeChar);
        }
        return buffer.toString();
    }

    @Test
    void testBase64ImpossibleSamplesDefault() throws DecoderException {
        assertFalse(bcodec.isStrictDecoding(), "Default decoding should be lenient");
        for (String sample : BASE64_IMPOSSIBLE_CASES) {
            bcodec.decode(sample);
        }
    }

    @Test
    void testBase64ImpossibleSamplesLenient() throws DecoderException {
        BCodec lenientCodec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.LENIENT);
        assertFalse(lenientCodec.isStrictDecoding(), "Lenient decoding should be false");
        for (String sample : BASE64_IMPOSSIBLE_CASES) {
            lenientCodec.decode(sample);
        }
    }

    @Test
    void testBase64ImpossibleSamplesStrict() {
        BCodec strictCodec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.STRICT);
        assertTrue(strictCodec.isStrictDecoding(), "Strict decoding should be true");
        for (String sample : BASE64_IMPOSSIBLE_CASES) {
            assertThrows(DecoderException.class, () -> strictCodec.decode(sample));
        }
    }

    @Test
    void testBasicEncodeDecode() throws Exception {
        String plainText = "Hello there";
        String encodedText = bcodec.encode(plainText);
        assertEquals("=?UTF-8?B?SGVsbG8gdGhlcmU=?=", encodedText, "Encoding should match expected Base64 format");
        assertEquals(plainText, bcodec.decode(encodedText), "Decoded text should match original");
    }

    @Test
    void testDecodeObjects() throws Exception {
        String encoded = "=?UTF-8?B?d2hhdCBub3Q=?=";
        String decoded = (String) bcodec.decode((Object) encoded);
        assertEquals("what not", decoded, "Decoded object should match expected string");

        assertNull(bcodec.decode((Object) null), "Decoding null object should return null");

        assertThrows(DecoderException.class, () -> bcodec.decode(Double.valueOf(3.0d)), "Decoding non-string should throw exception");
    }

    @Test
    void testDecodeStringWithNull() throws Exception {
        assertNull(bcodec.decode((String) null), "Decoding null string should return null");
    }

    @Test
    void testEncodeDecodeNull() throws Exception {
        assertNull(bcodec.encode((String) null), "Encoding null string should return null");
        assertNull(bcodec.decode((String) null), "Decoding null string should return null");
    }

    @Test
    void testEncodeObjects() throws Exception {
        String plainText = "what not";
        String encoded = (String) bcodec.encode((Object) plainText);
        assertEquals("=?UTF-8?B?d2hhdCBub3Q=?=", encoded, "Encoded object should match expected Base64 format");

        assertNull(bcodec.encode((Object) null), "Encoding null object should return null");

        assertThrows(EncoderException.class, () -> bcodec.encode(Double.valueOf(3.0d)), "Encoding non-string should throw exception");
    }

    @Test
    void testEncodeStringWithNull() throws Exception {
        assertNull(bcodec.encode((String) null, "charset"), "Encoding null string with charset should return null");
    }

    @Test
    void testInvalidEncoding() {
        assertThrows(UnsupportedCharsetException.class, () -> new BCodec("NONSENSE"), "Invalid charset should throw exception");
    }

    @Test
    void testNullInput() throws Exception {
        assertNull(bcodec.doDecoding(null), "Decoding null input should return null");
        assertNull(bcodec.doEncoding(null), "Encoding null input should return null");
    }

    @Test
    void testUTF8RoundTrip() throws Exception {
        String russianMessage = convertUnicodeToString(RUSSIAN_UNICODE);
        String swissGermanMessage = convertUnicodeToString(SWISS_GERMAN_UNICODE);

        BCodec utf8Codec = new BCodec(CharEncoding.UTF_8);

        assertEquals("=?UTF-8?B?0JLRgdC10Lxf0L/RgNC40LLQtdGC?=", utf8Codec.encode(russianMessage), "Encoded Russian message should match expected format");
        assertEquals("=?UTF-8?B?R3LDvGV6aV96w6Rtw6Q=?=", utf8Codec.encode(swissGermanMessage), "Encoded Swiss German message should match expected format");

        assertEquals(russianMessage, utf8Codec.decode(utf8Codec.encode(russianMessage)), "Decoded Russian message should match original");
        assertEquals(swissGermanMessage, utf8Codec.decode(utf8Codec.encode(swissGermanMessage)), "Decoded Swiss German message should match original");
    }
}