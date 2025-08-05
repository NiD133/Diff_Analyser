package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

import static org.junit.Assert.*;

/**
 * Provides well-structured and understandable tests for the {@link QuotedPrintableCodec}.
 * The tests are organized by method and functionality, focusing on clarity and maintainability.
 */
public class QuotedPrintableCodecTest {

    private static final String SAFE_TEXT = "Hello, this is a simple text.";
    private static final String UNSAFE_TEXT = "This text contains unsafe characters: \u00F1 \u00E1 \u00E7"; // ñ á ç
    private static final String ENCODED_UNSAFE_TEXT_UTF8 = "This text contains unsafe characters: =C3=B1 =C3=A1 =C3=A7";

    // A long string to test soft line breaks in strict mode (RFC 1521, 76-character line limit).
    private static final String LONG_TEXT = "This is a very long line of text that is designed to exceed the 76-character limit for Quoted-Printable encoding to test soft line breaks.";
    private static final String LONG_TEXT_ENCODED_STRICT = "This is a very long line of text that is designed to exceed the 76-character=\r\n limit for Quoted-Printable encoding to test soft line breaks.";

    // =================================================================
    // Constructor Tests
    // =================================================================

    @Test
    public void defaultConstructorShouldUseUtf8() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertEquals("UTF-8", codec.getDefaultCharset());
    }

    @Test
    public void charsetConstructorShouldSetTheCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(StandardCharsets.US_ASCII);
        assertEquals(StandardCharsets.US_ASCII, codec.getCharset());
    }

    @Test
    public void stringConstructorShouldSetTheCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-16");
        assertEquals(StandardCharsets.UTF_16, codec.getCharset());
    }

    @Test(expected = IllegalArgumentException.class)
    public void stringConstructorShouldThrowForNullCharsetName() {
        new QuotedPrintableCodec((String) null);
    }

    // =================================================================
    // Encoding Tests
    // =================================================================

    @Test
    public void shouldEncodeStringToPreserveSafeCharacters() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encoded = codec.encode(SAFE_TEXT);
        assertEquals(SAFE_TEXT, encoded);
    }

    @Test
    public void shouldEncodeStringWithUnsafeCharacters() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String encoded = codec.encode(UNSAFE_TEXT);
        assertEquals(ENCODED_UNSAFE_TEXT_UTF8, encoded);
    }

    @Test
    public void shouldEncodeStringWithSpecificCharset() throws EncoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "\u00C4\u00D6\u00DC"; // ÄÖÜ
        String encoded = codec.encode(input, "ISO-8859-1");
        assertEquals("=C4=D6=DC", encoded);
    }

    @Test
    public void strictEncodingShouldAddSoftLineBreaksForLongLines() throws EncoderException {
        QuotedPrintableCodec strictCodec = new QuotedPrintableCodec(true);
        String encoded = strictCodec.encode(LONG_TEXT);
        assertEquals(LONG_TEXT_ENCODED_STRICT, encoded);
    }

    @Test
    public void nonStrictEncodingShouldNotAddSoftLineBreaks() throws EncoderException {
        QuotedPrintableCodec nonStrictCodec = new QuotedPrintableCodec(false);
        String encoded = nonStrictCodec.encode(LONG_TEXT);
        // Non-strict encoding does not add line breaks
        assertEquals(LONG_TEXT, encoded);
    }

    @Test
    public void strictEncodingShouldEncodeTrailingWhitespace() throws EncoderException {
        QuotedPrintableCodec strictCodec = new QuotedPrintableCodec(true);
        assertEquals("Hello World=20", strictCodec.encode("Hello World "));
        assertEquals("Hello World=09", strictCodec.encode("Hello World\t"));
    }

    @Test
    public void shouldEncodeNullObjectToNull() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode((Object) null));
    }

    @Test
    public void shouldEncodeEmptyStringToEmptyString() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertEquals("", codec.encode(""));
    }

    @Test(expected = EncoderException.class)
    public void shouldThrowExceptionWhenEncodingInvalidObjectType() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode(new Object());
    }

    // =================================================================
    // Decoding Tests
    // =================================================================

    @Test
    public void shouldDecodeStringToPreserveSafeCharacters() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String decoded = codec.decode(SAFE_TEXT);
        assertEquals(SAFE_TEXT, decoded);
    }

    @Test
    public void shouldDecodeStringWithEncodedCharacters() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String decoded = codec.decode(ENCODED_UNSAFE_TEXT_UTF8);
        assertEquals(UNSAFE_TEXT, decoded);
    }

    @Test
    public void shouldDecodeStringWithSpecificCharset() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String decoded = codec.decode("=C4=D6=DC", "ISO-8859-1");
        assertEquals("\u00C4\u00D6\u00DC", decoded);
    }

    @Test
    public void shouldDecodeStringWithSoftLineBreaks() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String decoded = codec.decode(LONG_TEXT_ENCODED_STRICT);
        assertEquals(LONG_TEXT, decoded);
    }

    @Test
    public void shouldDecodeStringWithEncodedWhitespace() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertEquals("Hello World ", codec.decode("Hello World=20"));
        assertEquals("Hello World\t", codec.decode("Hello World=09"));
    }

    @Test
    public void shouldDecodeNullObjectToNull() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.decode((Object) null));
    }

    @Test
    public void shouldDecodeEmptyStringToEmptyString() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertEquals("", codec.decode(""));
    }

    @Test(expected = DecoderException.class)
    public void shouldThrowExceptionWhenDecodingInvalidObjectType() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(new Object());
    }

    @Test(expected = DecoderException.class)
    public void shouldThrowExceptionForInvalidEscapeSequence() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        // 'G' is not a valid hexadecimal character
        codec.decode("=G0");
    }

    @Test(expected = DecoderException.class)
    public void shouldThrowExceptionForIncompleteEscapeSequence() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        // Escape character at the end of the string
        codec.decode("some text=");
    }

    // =================================================================
    // Byte Array and Static Method Tests
    // =================================================================

    @Test
    public void shouldPerformRoundTripSuccessfully() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] inputBytes = UNSAFE_TEXT.getBytes(StandardCharsets.UTF_8);

        byte[] encodedBytes = codec.encode(inputBytes);
        assertEquals(ENCODED_UNSAFE_TEXT_UTF8, new String(encodedBytes, StandardCharsets.US_ASCII));

        byte[] decodedBytes = codec.decode(encodedBytes);
        assertArrayEquals(inputBytes, decodedBytes);
        assertEquals(UNSAFE_TEXT, new String(decodedBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void staticDecodeShouldHandleNullAndEmpty() throws DecoderException {
        assertNull(QuotedPrintableCodec.decodeQuotedPrintable(null));
        assertArrayEquals(new byte[0], QuotedPrintableCodec.decodeQuotedPrintable(new byte[0]));
    }

    @Test
    public void staticEncodeShouldHandleNullAndEmpty() {
        assertNull(QuotedPrintableCodec.encodeQuotedPrintable(null, null));
        assertArrayEquals(new byte[0], QuotedPrintableCodec.encodeQuotedPrintable(null, new byte[0]));
    }

    @Test
    public void staticEncodeWithCustomPrintableSet() {
        // Arrange: Create a BitSet where only the character 'A' (65) is considered printable.
        BitSet printable = new BitSet();
        printable.set('A');
        byte[] input = "ABA".getBytes(StandardCharsets.US_ASCII); // 65, 66, 65

        // Act: Encode the input. 'A' should remain, 'B' should be encoded.
        byte[] encoded = QuotedPrintableCodec.encodeQuotedPrintable(printable, input);

        // Assert
        assertArrayEquals("A=42A".getBytes(StandardCharsets.US_ASCII), encoded);
    }

    @Test
    public void staticEncodeWithEmptyPrintableSetShouldEncodeAllBytes() {
        // Arrange: An empty BitSet means no characters are printable.
        BitSet printable = new BitSet();
        byte[] input = "ABC".getBytes(StandardCharsets.US_ASCII); // 65, 66, 67

        // Act
        byte[] encoded = QuotedPrintableCodec.encodeQuotedPrintable(printable, input);

        // Assert
        assertArrayEquals("=41=42=43".getBytes(StandardCharsets.US_ASCII), encoded);
    }
}