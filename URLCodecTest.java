package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the URLCodec class, which handles URL encoding and decoding.
 */
class URLCodecTest {

    // Unicode representations for test strings
    private static final int[] SWISS_GERMAN_UNICODE = { 0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 };
    private static final int[] RUSSIAN_UNICODE = { 0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 };

    /**
     * Constructs a string from an array of Unicode code points.
     *
     * @param unicodeChars Array of Unicode code points.
     * @return Constructed string.
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
    void testBasicEncodeDecode() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        final String plainText = "Hello there!";
        final String encodedText = urlCodec.encode(plainText);

        assertEquals("Hello+there%21", encodedText, "Basic URL encoding test");
        assertEquals(plainText, urlCodec.decode(encodedText), "Basic URL decoding test");
        validateCodecState(urlCodec);
    }

    @Test
    void testDecodeInvalidInputs() throws Exception {
        final URLCodec urlCodec = new URLCodec();

        assertThrows(DecoderException.class, () -> urlCodec.decode("%"));
        assertThrows(DecoderException.class, () -> urlCodec.decode("%A"));
        assertThrows(DecoderException.class, () -> urlCodec.decode("%WW")); // Invalid first char after %
        assertThrows(DecoderException.class, () -> urlCodec.decode("%0W")); // Invalid second char after %

        validateCodecState(urlCodec);
    }

    @Test
    void testDecodeInvalidContent() throws DecoderException {
        final String swissGermanText = constructStringFromUnicode(SWISS_GERMAN_UNICODE);
        final URLCodec urlCodec = new URLCodec();
        final byte[] inputBytes = swissGermanText.getBytes(StandardCharsets.ISO_8859_1);
        final byte[] outputBytes = urlCodec.decode(inputBytes);

        assertEquals(inputBytes.length, outputBytes.length);
        for (int i = 0; i < inputBytes.length; i++) {
            assertEquals(inputBytes[i], outputBytes[i]);
        }

        validateCodecState(urlCodec);
    }

    @Test
    void testDecodeObjects() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        final String encodedText = "Hello+there%21";

        // Decode from String
        String decodedText = (String) urlCodec.decode((Object) encodedText);
        assertEquals("Hello there!", decodedText, "Basic URL decoding test");

        // Decode from byte array
        final byte[] encodedBytes = encodedText.getBytes(StandardCharsets.UTF_8);
        final byte[] decodedBytes = (byte[]) urlCodec.decode((Object) encodedBytes);
        decodedText = new String(decodedBytes);
        assertEquals("Hello there!", decodedText, "Basic URL decoding test");

        // Decode null object
        final Object result = urlCodec.decode((Object) null);
        assertNull(result, "Decoding a null Object should return null");

        // Decode invalid object type
        assertThrows(DecoderException.class, () -> urlCodec.decode(Double.valueOf(3.0d)), "Decoding a Double object should cause an exception.");

        validateCodecState(urlCodec);
    }

    @Test
    void testDecodeStringWithNull() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        final String testString = null;
        final String result = urlCodec.decode(testString, "charset");

        assertNull(result, "Decoding a null string should return null");
    }

    @Test
    void testDecodeWithNullArray() throws Exception {
        final byte[] nullBytes = null;
        final byte[] result = URLCodec.decodeUrl(nullBytes);

        assertNull(result, "Decoding a null byte array should return null");
    }

    @Test
    void testDefaultEncoding() throws Exception {
        final String plainText = "Hello there!";
        final URLCodec urlCodec = new URLCodec("UnicodeBig");

        // Encode to ensure no issues with default encoding
        urlCodec.encode(plainText);

        final String encoded1 = urlCodec.encode(plainText, "UnicodeBig");
        final String encoded2 = urlCodec.encode(plainText);

        assertEquals(encoded1, encoded2, "Encoding with specified and default charset should match");
        validateCodecState(urlCodec);
    }

    @Test
    void testEncodeDecodeNull() throws Exception {
        final URLCodec urlCodec = new URLCodec();

        assertNull(urlCodec.encode((String) null), "Encoding a null string should return null");
        assertNull(urlCodec.decode((String) null), "Decoding a null string should return null");

        validateCodecState(urlCodec);
    }

    @Test
    void testEncodeNull() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        final byte[] nullBytes = null;
        final byte[] encodedBytes = urlCodec.encode(nullBytes);

        assertNull(encodedBytes, "Encoding a null byte array should return null");
        validateCodecState(urlCodec);
    }

    @Test
    void testEncodeObjects() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        final String plainText = "Hello there!";

        // Encode from String
        String encodedText = (String) urlCodec.encode((Object) plainText);
        assertEquals("Hello+there%21", encodedText, "Basic URL encoding test");

        // Encode from byte array
        final byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
        final byte[] encodedBytes = (byte[]) urlCodec.encode((Object) plainBytes);
        encodedText = new String(encodedBytes);
        assertEquals("Hello+there%21", encodedText, "Basic URL encoding test");

        // Encode null object
        final Object result = urlCodec.encode((Object) null);
        assertNull(result, "Encoding a null Object should return null");

        // Encode invalid object type
        assertThrows(EncoderException.class, () -> urlCodec.encode(Double.valueOf(3.0d)), "Encoding a Double object should cause an exception.");

        validateCodecState(urlCodec);
    }

    @Test
    void testEncodeStringWithNull() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        final String testString = null;
        final String result = urlCodec.encode(testString, "charset");

        assertNull(result, "Encoding a null string should return null");
    }

    @Test
    void testEncodeUrlWithNullBitSet() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        final String plainText = "Hello there!";
        final String encodedText = new String(URLCodec.encodeUrl(null, plainText.getBytes(StandardCharsets.UTF_8)));

        assertEquals("Hello+there%21", encodedText, "Basic URL encoding test");
        assertEquals(plainText, urlCodec.decode(encodedText), "Basic URL decoding test");

        validateCodecState(urlCodec);
    }

    @Test
    void testInvalidEncoding() {
        final URLCodec urlCodec = new URLCodec("NONSENSE");
        final String plainText = "Hello there!";

        assertThrows(EncoderException.class, () -> urlCodec.encode(plainText), "Encoding with a bogus charset should cause an exception.");
        assertThrows(DecoderException.class, () -> urlCodec.decode(plainText), "Decoding with a bogus charset should cause an exception.");

        validateCodecState(urlCodec);
    }

    @Test
    void testSafeCharEncodeDecode() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        final String safeChars = "abc123_-.*";

        final String encodedText = urlCodec.encode(safeChars);
        assertEquals(safeChars, encodedText, "Safe chars should not be encoded");

        final String decodedText = urlCodec.decode(encodedText);
        assertEquals(safeChars, decodedText, "Safe chars should decode back to original");

        validateCodecState(urlCodec);
    }

    @Test
    void testUnsafeEncodeDecode() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        final String unsafeChars = "~!@#$%^&()+{}\"\\;:`,/[]";

        final String encodedText = urlCodec.encode(unsafeChars);
        assertEquals("%7E%21%40%23%24%25%5E%26%28%29%2B%7B%7D%22%5C%3B%3A%60%2C%2F%5B%5D", encodedText, "Unsafe chars URL encoding test");

        final String decodedText = urlCodec.decode(encodedText);
        assertEquals(unsafeChars, decodedText, "Unsafe chars should decode back to original");

        validateCodecState(urlCodec);
    }

    @Test
    void testUTF8RoundTrip() throws Exception {
        final String russianText = constructStringFromUnicode(RUSSIAN_UNICODE);
        final String swissGermanText = constructStringFromUnicode(SWISS_GERMAN_UNICODE);

        final URLCodec urlCodec = new URLCodec();

        // Encode and decode Russian text
        final String encodedRussian = urlCodec.encode(russianText, CharEncoding.UTF_8);
        assertEquals("%D0%92%D1%81%D0%B5%D0%BC_%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82", encodedRussian);
        assertEquals(russianText, urlCodec.decode(encodedRussian, CharEncoding.UTF_8));

        // Encode and decode Swiss German text
        final String encodedSwissGerman = urlCodec.encode(swissGermanText, CharEncoding.UTF_8);
        assertEquals("Gr%C3%BCezi_z%C3%A4m%C3%A4", encodedSwissGerman);
        assertEquals(swissGermanText, urlCodec.decode(encodedSwissGerman, CharEncoding.UTF_8));

        validateCodecState(urlCodec);
    }

    /**
     * Validates the state of the URLCodec instance.
     * Currently, this method does not perform any checks.
     *
     * @param urlCodec The URLCodec instance to validate.
     */
    private void validateCodecState(final URLCodec urlCodec) {
        // No state validation needed at the moment.
    }
}