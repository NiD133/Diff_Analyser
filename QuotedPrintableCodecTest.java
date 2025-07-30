package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for QuotedPrintableCodec.
 */
class QuotedPrintableCodecTest {

    private static final int[] SWISS_GERMAN_UNICODE = { 0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 };
    private static final int[] RUSSIAN_UNICODE = { 0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 };

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
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String input = "= Hello there =\r\n";
        final String encoded = codec.encode(input);
        final String expectedEncoded = "=3D Hello there =3D=0D=0A";
        assertEquals(expectedEncoded, encoded, "Encoding failed");
        assertEquals(input, codec.decode(encoded), "Decoding failed");
    }

    @Test
    void testDecodeInvalidInput() {
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertThrows(DecoderException.class, () -> codec.decode("="));
        assertThrows(DecoderException.class, () -> codec.decode("=A"));
        assertThrows(DecoderException.class, () -> codec.decode("=WW"));
    }

    @Test
    void testDecodeVariousObjects() throws Exception {
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String input = "1+1 =3D 2";
        String decoded = (String) codec.decode((Object) input);
        assertEquals("1+1 = 2", decoded, "Decoding failed for string input");

        final byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        final byte[] decodedBytes = (byte[]) codec.decode((Object) inputBytes);
        decoded = new String(decodedBytes);
        assertEquals("1+1 = 2", decoded, "Decoding failed for byte array input");

        assertNull(codec.decode((Object) null), "Decoding null should return null");

        assertThrows(DecoderException.class, () -> codec.decode(Double.valueOf(3.0d)), "Decoding a Double should throw exception");
    }

    @Test
    void testDecodeNullString() throws Exception {
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.decode((String) null, "charset"), "Decoding null string should return null");
    }

    @Test
    void testDecodeNullByteArray() throws Exception {
        assertNull(QuotedPrintableCodec.decodeQuotedPrintable(null), "Decoding null byte array should return null");
    }

    @Test
    void testDefaultEncodingConsistency() throws Exception {
        final String input = "Hello there!";
        final QuotedPrintableCodec codec = new QuotedPrintableCodec("UnicodeBig");
        codec.encode(input); // Workaround for Java 1.2.2 quirk
        assertEquals(codec.encode(input, "UnicodeBig"), codec.encode(input), "Encoded strings should match");
    }

    @Test
    void testEncodeDecodeNullString() throws Exception {
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode((String) null), "Encoding null string should return null");
        assertNull(codec.decode((String) null), "Decoding null string should return null");
    }

    @Test
    void testEncodeNullByteArray() throws Exception {
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode((byte[]) null), "Encoding null byte array should return null");
    }

    @Test
    void testEncodeVariousObjects() throws Exception {
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String input = "1+1 = 2";
        String encoded = (String) codec.encode((Object) input);
        assertEquals("1+1 =3D 2", encoded, "Encoding failed for string input");

        final byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        final byte[] encodedBytes = (byte[]) codec.encode((Object) inputBytes);
        encoded = new String(encodedBytes);
        assertEquals("1+1 =3D 2", encoded, "Encoding failed for byte array input");

        assertNull(codec.encode((Object) null), "Encoding null object should return null");

        assertThrows(EncoderException.class, () -> codec.encode(Double.valueOf(3.0d)), "Encoding a Double should throw exception");
    }

    @Test
    void testEncodeNullString() throws Exception {
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode((String) null, "charset"), "Encoding null string should return null");
    }

    @Test
    void testEncodeWithNullBitSet() throws Exception {
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String input = "1+1 = 2";
        final String encoded = new String(QuotedPrintableCodec.encodeQuotedPrintable(null, input.getBytes(StandardCharsets.UTF_8)));
        assertEquals("1+1 =3D 2", encoded, "Encoding failed with null BitSet");
        assertEquals(input, codec.decode(encoded), "Decoding failed with null BitSet");
    }

    @Test
    void testFinalBytesEncoding() throws Exception {
        final String input = "This is a example of a quoted=printable text file. There is no tt";
        final String expected = "This is a example of a quoted=3Dprintable text file. There is no tt";
        assertEquals(expected, new QuotedPrintableCodec(true).encode(input), "Final bytes encoding failed");
    }

    @Test
    void testInvalidCharset() {
        assertThrows(UnsupportedCharsetException.class, () -> new QuotedPrintableCodec("NONSENSE"), "Invalid charset should throw exception");
    }

    @Test
    void testSafeCharactersEncodeDecode() throws Exception {
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String input = "abc123_-.*~!@#$%^&()+{}\"\\;:`,/[]";
        final String encoded = codec.encode(input);
        assertEquals(input, encoded, "Safe characters encoding failed");
        assertEquals(input, codec.decode(encoded), "Safe characters decoding failed");
    }

    @Test
    void testSkipUnencodedCRLF() throws Exception {
        final String input = "CRLF in an\n encoded text should be=20=\r\n\rskipped in the\r decoding.";
        final String expected = "CRLF in an encoded text should be skipped in the decoding.";
        final QuotedPrintableCodec codec = new QuotedPrintableCodec(true);
        assertEquals(expected, codec.decode(input), "CRLF decoding failed");
        assertEquals(expected, codec.decode(codec.encode(expected)), "CRLF round-trip failed");
    }

    @Test
    void testSoftLineBreakDecoding() throws Exception {
        final String input = "If you believe that truth=3Dbeauty, then surely=20=\r\nmathematics is the most beautiful branch of philosophy.";
        final String expected = "If you believe that truth=beauty, then surely mathematics is the most beautiful branch of philosophy.";
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertEquals(expected, codec.decode(input), "Soft line break decoding failed");
        assertEquals(expected, codec.decode(codec.encode(expected)), "Soft line break round-trip failed");
    }

    @Test
    void testSoftLineBreakEncoding() throws Exception {
        final String input = "If you believe that truth=3Dbeauty, then surely mathematics is the most b=\r\neautiful branch of philosophy.";
        final String expected = "If you believe that truth=beauty, then surely mathematics is the most beautiful branch of philosophy.";
        final QuotedPrintableCodec codec = new QuotedPrintableCodec(true);
        assertEquals(input, codec.encode(expected), "Soft line break encoding failed");
        assertEquals(input, codec.encode(codec.decode(input)), "Soft line break round-trip failed");
    }

    @Test
    void testTooShortByteArrayEncoding() throws Exception {
        final QuotedPrintableCodec codec = new QuotedPrintableCodec(true);
        assertNull(codec.encode("AA"), "Encoding too short byte array should return null");
    }

    @Test
    void testTrailingSpecialCharacters() throws Exception {
        final QuotedPrintableCodec codec = new QuotedPrintableCodec(true);

        String input = "This is a example of a quoted-printable text file. This might contain sp=cial chars.";
        String expected = "This is a example of a quoted-printable text file. This might contain sp=3D=\r\ncial chars.";
        assertEquals(expected, codec.encode(input), "Trailing special characters encoding failed");

        input = "This is a example of a quoted-printable text file. This might contain ta\tbs as well.";
        expected = "This is a example of a quoted-printable text file. This might contain ta=09=\r\nbs as well.";
        assertEquals(expected, codec.encode(input), "Trailing tab characters encoding failed");
    }

    @Test
    void testUltimateSoftBreakEncoding() throws Exception {
        final QuotedPrintableCodec codec = new QuotedPrintableCodec(true);

        String input = "This is a example of a quoted-printable text file. There is no end to it\t";
        String expected = "This is a example of a quoted-printable text file. There is no end to i=\r\nt=09";
        assertEquals(expected, codec.encode(input), "Ultimate soft break encoding failed");

        input = "This is a example of a quoted-printable text file. There is no end to it ";
        expected = "This is a example of a quoted-printable text file. There is no end to i=\r\nt=20";
        assertEquals(expected, codec.encode(input), "Trailing space encoding failed");

        input = "This is a example of a quoted-printable text file. There is no end to   ";
        expected = "This is a example of a quoted-printable text file. There is no end to=20=\r\n =20";
        assertEquals(expected, codec.encode(input), "Multiple trailing spaces encoding failed");

        input = "This is a example of a quoted-printable text file. There is no end to=  ";
        expected = "This is a example of a quoted-printable text file. There is no end to=3D=\r\n =20";
        assertEquals(expected, codec.encode(input), "Trailing equals sign encoding failed");
    }

    @Test
    void testUnsafeCharactersEncodeDecode() throws Exception {
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String input = "=\r\n";
        final String encoded = codec.encode(input);
        assertEquals("=3D=0D=0A", encoded, "Unsafe characters encoding failed");
        assertEquals(input, codec.decode(encoded), "Unsafe characters decoding failed");
    }

    @Test
    void testUTF8RoundTripEncoding() throws Exception {
        final String russianMessage = constructStringFromUnicode(RUSSIAN_UNICODE);
        final String swissGermanMessage = constructStringFromUnicode(SWISS_GERMAN_UNICODE);

        final QuotedPrintableCodec codec = new QuotedPrintableCodec();

        assertEquals("=D0=92=D1=81=D0=B5=D0=BC_=D0=BF=D1=80=D0=B8=D0=B2=D0=B5=D1=82", codec.encode(russianMessage, CharEncoding.UTF_8), "Russian message encoding failed");
        assertEquals("Gr=C3=BCezi_z=C3=A4m=C3=A4", codec.encode(swissGermanMessage, CharEncoding.UTF_8), "Swiss German message encoding failed");

        assertEquals(russianMessage, codec.decode(codec.encode(russianMessage, CharEncoding.UTF_8), CharEncoding.UTF_8), "Russian message round-trip failed");
        assertEquals(swissGermanMessage, codec.decode(codec.encode(swissGermanMessage, CharEncoding.UTF_8), CharEncoding.UTF_8), "Swiss German message round-trip failed");
    }
}