package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for QuotedPrintableCodec focusing on readability and explicit intent.
 */
class QuotedPrintableCodecTest {

    private static final Charset UTF8 = StandardCharsets.UTF_8;

    // Known strings and their encoded forms
    private static final String PLAIN_EQUALS_AND_CRLF = "= Hello there =\r\n";
    private static final String ENCODED_EQUALS_AND_CRLF = "=3D Hello there =3D=0D=0A";

    private static final int[] SWISS_GERMAN_STUFF_UNICODE = { 0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 };
    private static final int[] RUSSIAN_STUFF_UNICODE = { 0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 };

    private static QuotedPrintableCodec codec() {
        return new QuotedPrintableCodec();
    }

    private static QuotedPrintableCodec strictCodec() {
        return new QuotedPrintableCodec(true);
    }

    private static String fromCodePoints(final int[] cps) {
        return cps == null ? null : new String(cps, 0, cps.length);
    }

    private static byte[] utf8Bytes(final String s) {
        return s == null ? null : s.getBytes(UTF8);
    }

    private static String utf8String(final byte[] bytes) {
        return bytes == null ? null : new String(bytes, UTF8);
    }

    private static void assertRoundTrip(final QuotedPrintableCodec codec, final String plain) throws Exception {
        final String encoded = codec.encode(plain);
        assertEquals(plain, codec.decode(encoded), "Encoded then decoded string should match original");
    }

    @Test
    void testEncodeDecode_basicEqualityAndCRLF() throws Exception {
        final QuotedPrintableCodec codec = codec();
        final String encoded = codec.encode(PLAIN_EQUALS_AND_CRLF);

        assertEquals(ENCODED_EQUALS_AND_CRLF, encoded, "Basic encoding should escape '=' and CR/LF");
        assertEquals(PLAIN_EQUALS_AND_CRLF, codec.decode(encoded), "Decoding should restore original");
    }

    @ParameterizedTest
    @ValueSource(strings = { "=", "=A", "=WW" })
    void testDecode_rejectsInvalidEscapeSequences(final String invalid) {
        final QuotedPrintableCodec codec = codec();
        assertThrows(DecoderException.class, () -> codec.decode(invalid), "Invalid escape sequence should throw");
    }

    @Test
    void testDecode_objectVariants() throws Exception {
        final QuotedPrintableCodec codec = codec();
        final String qp = "1+1 =3D 2";

        final String decodedFromString = (String) codec.decode((Object) qp);
        assertEquals("1+1 = 2", decodedFromString);

        final byte[] decodedFromBytes = (byte[]) codec.decode((Object) qp.getBytes(UTF8));
        assertEquals("1+1 = 2", utf8String(decodedFromBytes));

        assertNull(codec.decode((Object) null), "Decoding null object returns null");

        assertThrows(DecoderException.class, () -> codec.decode(Double.valueOf(3.0d)), "Non String/byte[] should throw");
    }

    @Test
    void testDecode_stringNullReturnsNull() throws Exception {
        final QuotedPrintableCodec codec = codec();
        assertNull(codec.decode((String) null, "charset"));
    }

    @Test
    void testDecode_nullByteArrayReturnsNull() throws Exception {
        assertNull(QuotedPrintableCodec.decodeQuotedPrintable(null));
    }

    @Test
    void testEncode_defaultCharsetConsistency() throws Exception {
        final String plain = "Hello there!";
        final QuotedPrintableCodec codec = new QuotedPrintableCodec("UnicodeBig");

        // Warm up as in original test (historic JDK quirk)
        codec.encode(plain);

        final String encodedWithExplicit = codec.encode(plain, "UnicodeBig");
        final String encodedWithDefault = codec.encode(plain);

        assertEquals(encodedWithExplicit, encodedWithDefault, "Encoding should be consistent with default charset");
    }

    @Test
    void testEncodeDecode_nullStringReturnsNull() throws Exception {
        final QuotedPrintableCodec codec = codec();
        assertNull(codec.encode((String) null));
        assertNull(codec.decode((String) null));
    }

    @Test
    void testEncode_nullByteArrayReturnsNull() throws Exception {
        final QuotedPrintableCodec codec = codec();
        assertNull(codec.encode((byte[]) null));
    }

    @Test
    void testEncode_objectVariants() throws Exception {
        final QuotedPrintableCodec codec = codec();
        final String plain = "1+1 = 2";

        final String encodedFromString = (String) codec.encode((Object) plain);
        assertEquals("1+1 =3D 2", encodedFromString);

        final byte[] encodedFromBytes = (byte[]) codec.encode((Object) plain.getBytes(UTF8));
        assertEquals("1+1 =3D 2", utf8String(encodedFromBytes));

        assertNull(codec.encode((Object) null), "Encoding null object returns null");

        assertThrows(EncoderException.class, () -> codec.encode(Double.valueOf(3.0d)), "Non String/byte[] should throw");
    }

    @Test
    void testEncode_stringNullReturnsNull() throws Exception {
        final QuotedPrintableCodec codec = codec();
        assertNull(codec.encode((String) null, "charset"));
    }

    @Test
    void testStaticEncodeQuotedPrintable_nullBitSetUsesDefaultPrintableSet() throws Exception {
        final QuotedPrintableCodec codec = codec();
        final String plain = "1+1 = 2";

        final String encoded = utf8String(QuotedPrintableCodec.encodeQuotedPrintable(null, utf8Bytes(plain)));
        assertEquals("1+1 =3D 2", encoded);
        assertEquals(plain, codec.decode(encoded));
    }

    @Test
    void testEncode_equalsSignInsideWord_isEscaped() throws Exception {
        final String plain = "This is a example of a quoted=printable text file. There is no tt";
        final String expected = "This is a example of a quoted=3Dprintable text file. There is no tt";

        assertEquals(expected, new QuotedPrintableCodec(true).encode(plain));
    }

    @Test
    void testConstructor_rejectsUnknownCharset() {
        assertThrows(UnsupportedCharsetException.class, () -> new QuotedPrintableCodec("NONSENSE"));
    }

    @Test
    void testEncodeDecode_safeCharactersUnchanged() throws Exception {
        final QuotedPrintableCodec codec = codec();
        final String safe = "abc123_-.*~!@#$%^&()+{}\"\\;:`,/[]";

        final String encoded = codec.encode(safe);
        assertEquals(safe, encoded, "Safe characters must remain unchanged");
        assertEquals(safe, codec.decode(encoded));
    }

    @Test
    void testDecode_skipsUnencodedCRLFInStrictMode() throws Exception {
        final String input = "CRLF in an\n encoded text should be=20=\r\n\rskipped in the\r decoding.";
        final String expected = "CRLF in an encoded text should be skipped in the decoding.";

        final QuotedPrintableCodec strict = strictCodec();
        assertEquals(expected, strict.decode(input));

        final String encoded = strict.encode(expected);
        assertEquals(expected, strict.decode(encoded));
    }

    @Test
    void testDecode_softLineBreaksAreRemoved() throws Exception {
        final String qp = "If you believe that truth=3Dbeauty, then surely=20=\r\nmathematics is the most beautiful branch of philosophy.";
        final String expected = "If you believe that truth=beauty, then surely mathematics is the most beautiful branch of philosophy.";

        final QuotedPrintableCodec codec = codec();
        assertEquals(expected, codec.decode(qp));

        assertRoundTrip(codec, expected);
    }

    @Test
    void testEncode_strictModeInsertsSoftLineBreaks() throws Exception {
        final String qp = "If you believe that truth=3Dbeauty, then surely mathematics is the most b=\r\neautiful branch of philosophy.";
        final String plain = "If you believe that truth=beauty, then surely mathematics is the most beautiful branch of philosophy.";

        final QuotedPrintableCodec strict = strictCodec();
        assertEquals(qp, strict.encode(plain));

        final String decoded = strict.decode(qp);
        assertEquals(qp, strict.encode(decoded), "Re-encoding decoded text should reproduce original wrapping");
    }

    @Test
    void testEncode_strictModeTooShortInputReturnsNull() throws Exception {
        final QuotedPrintableCodec strict = strictCodec();
        assertNull(strict.encode("AA"), "Short input triggers null per original behavior");
    }

    @Test
    void testEncode_strictModeTrailingSpecialCharsWrappedCorrectly() throws Exception {
        final QuotedPrintableCodec strict = strictCodec();

        String plain = "This is a example of a quoted-printable text file. This might contain sp=cial chars.";
        String expected = "This is a example of a quoted-printable text file. This might contain sp=3D=\r\ncial chars.";
        assertEquals(expected, strict.encode(plain));

        plain = "This is a example of a quoted-printable text file. This might contain ta\tbs as well.";
        expected = "This is a example of a quoted-printable text file. This might contain ta=09=\r\nbs as well.";
        assertEquals(expected, strict.encode(plain));
    }

    @Test
    void testEncode_strictModeWhitespaceAndNonPrintableBeforeSoftBreak() throws Exception {
        final QuotedPrintableCodec strict = strictCodec();

        String plain = "This is a example of a quoted-printable text file. There is no end to it\t";
        String expected = "This is a example of a quoted-printable text file. There is no end to i=\r\nt=09";
        assertEquals(expected, strict.encode(plain));

        plain = "This is a example of a quoted-printable text file. There is no end to it ";
        expected = "This is a example of a quoted-printable text file. There is no end to i=\r\nt=20";
        assertEquals(expected, strict.encode(plain));

        // whitespace before soft break
        plain = "This is a example of a quoted-printable text file. There is no end to   ";
        expected = "This is a example of a quoted-printable text file. There is no end to=20=\r\n =20";
        assertEquals(expected, strict.encode(plain));

        // non-printable character before soft break
        plain = "This is a example of a quoted-printable text file. There is no end to=  ";
        expected = "This is a example of a quoted-printable text file. There is no end to=3D=\r\n =20";
        assertEquals(expected, strict.encode(plain));
    }

    @Test
    void testEncodeDecode_unsafeCharactersEncoded() throws Exception {
        final QuotedPrintableCodec codec = codec();
        final String unsafe = "=\r\n";

        final String encoded = codec.encode(unsafe);
        assertEquals("=3D=0D=0A", encoded, "Unsafe characters must be encoded");
        assertEquals(unsafe, codec.decode(encoded));
    }

    @Test
    void testUTF8_roundTripAndKnownEncodings() throws Exception {
        final String ru = fromCodePoints(RUSSIAN_STUFF_UNICODE);
        final String ch = fromCodePoints(SWISS_GERMAN_STUFF_UNICODE);

        final QuotedPrintableCodec codec = codec();

        assertEquals("=D0=92=D1=81=D0=B5=D0=BC_=D0=BF=D1=80=D0=B8=D0=B2=D0=B5=D1=82", codec.encode(ru, CharEncoding.UTF_8));
        assertEquals("Gr=C3=BCezi_z=C3=A4m=C3=A4", codec.encode(ch, CharEncoding.UTF_8));

        assertEquals(ru, codec.decode(codec.encode(ru, CharEncoding.UTF_8), CharEncoding.UTF_8));
        assertEquals(ch, codec.decode(codec.encode(ch, CharEncoding.UTF_8), CharEncoding.UTF_8));
    }
}