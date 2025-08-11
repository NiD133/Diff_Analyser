package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;

import static org.junit.Assert.*;

/**
 * Readable, behavior-oriented tests for QuotedPrintableCodec.
 *
 * Test layout:
 * - Constructors and basic getters
 * - Happy-path encode/decode for strings and bytes
 * - Strict vs non-strict behavior (trailing space)
 * - Null handling
 * - Error conditions (malformed input, wrong types, unsupported charsets)
 * - Static helpers encodeQuotedPrintable/decodeQuotedPrintable
 */
public class QuotedPrintableCodecTest {

    // ---------------------------------------------------------------------
    // Constructors and getters
    // ---------------------------------------------------------------------

    @Test
    public void defaultConstructor_usesUtf8_and_isNonStrict() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertEquals("UTF-8", codec.getDefaultCharset());
        assertEquals(StandardCharsets.UTF_8, codec.getCharset());
    }

    @Test
    public void constructor_withCharset_and_strictFlag() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(StandardCharsets.ISO_8859_1, true);
        assertEquals(StandardCharsets.ISO_8859_1, codec.getCharset());
        // getDefaultCharset is expected to return a name (String) of the default charset
        assertEquals("ISO-8859-1", codec.getDefaultCharset());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_nullCharsetName_throwsIAE() {
        new QuotedPrintableCodec((String) null);
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void constructor_illegalCharsetName_throwsIllegalCharsetNameException() {
        new QuotedPrintableCodec("bad charset name!");
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void constructor_unsupportedCharset_throwsUnsupportedCharsetException() {
        new QuotedPrintableCodec("X-Does-Not-Exist-12345");
    }

    @Test
    public void getCharset_canBeNull_ifConstructedWithNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);
        assertNull(codec.getCharset());
    }

    @Test(expected = NullPointerException.class)
    public void getDefaultCharset_throwsNpe_ifConstructedWithNullCharset() {
        // Behavior matches EvoSuite test: calling getDefaultCharset() when constructed with null should throw NPE.
        QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);
        codec.getDefaultCharset();
    }

    // ---------------------------------------------------------------------
    // Happy path: encode/decode strings
    // ---------------------------------------------------------------------

    @Test
    public void encodeDecode_roundTrip_asciiText_defaultConstructor() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(); // UTF-8, non-strict
        String original = "Hello, world! 1234 -=_./?";
        String encoded = codec.encode(original);
        String decoded = codec.decode(encoded);
        assertEquals(original, decoded);
    }

    @Test
    public void encodeDecode_roundTrip_utf8Text_explicitCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(StandardCharsets.UTF_8);
        String original = "Héllo — Καλημέρα — こんにちは"; // contains non-ASCII
        String encoded = codec.encode(original, StandardCharsets.UTF_8);
        String decoded = codec.decode(encoded, StandardCharsets.UTF_8);
        assertEquals(original, decoded);
    }

    // ---------------------------------------------------------------------
    // Strict vs non-strict behavior
    // ---------------------------------------------------------------------

    @Test
    public void strictMode_encodesTrailingSpace() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(true); // strict
        String original = "Ends with space ";
        String encoded = codec.encode(original);
        // Trailing space should be encoded as =20 in strict mode
        assertTrue("Expected trailing space to be encoded as =20", encoded.endsWith("=20"));
        assertEquals(original, codec.decode(encoded));
    }

    @Test
    public void nonStrictMode_keepsInternalSpaces() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(false); // non-strict
        String original = "Text with internal spaces";
        String encoded = codec.encode(original);
        // No special expectations about internal spaces; round trip must hold.
        assertEquals(original, codec.decode(encoded));
    }

    // ---------------------------------------------------------------------
    // Happy path: encode/decode bytes
    // ---------------------------------------------------------------------

    @Test
    public void encodeDecode_roundTrip_bytes() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(StandardCharsets.UTF_8, true);
        byte[] original = "binary \u0000 data \u007F".getBytes(StandardCharsets.UTF_8);
        byte[] encoded = codec.encode(original);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(original, decoded);
        // Ensure a new array is returned (defensive behavior)
        assertNotSame(original, encoded);
        assertNotSame(original, decoded);
    }

    // ---------------------------------------------------------------------
    // Null handling
    // ---------------------------------------------------------------------

    @Test
    public void encode_string_null_returnsNull() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode((String) null));
        assertNull(codec.encode((String) null, StandardCharsets.UTF_8));
        assertNull(codec.encode((String) null, "UTF-8"));
    }

    @Test
    public void decode_string_null_returnsNull() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.decode((String) null));
        assertNull(codec.decode((String) null, StandardCharsets.UTF_8));
        assertNull(codec.decode((String) null, "UTF-8"));
    }

    @Test
    public void encode_bytes_null_returnsNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode((byte[]) null));
    }

    @Test
    public void decode_bytes_null_returnsNull() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.decode((byte[]) null));
    }

    // ---------------------------------------------------------------------
    // Error handling
    // ---------------------------------------------------------------------

    @Test(expected = DecoderException.class)
    public void decode_malformedSequence_throwsDecoderException() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode("Bad=GGSequence"); // 'GG' is not valid hex
    }

    @Test(expected = DecoderException.class)
    public void decode_bytes_malformedSequence_throwsDecoderException() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(new byte[]{'='}); // lone '='
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void decode_withUnsupportedCharsetName_throwsUnsupportedEncoding() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode("Hello", "X-Does-Not-Exist-12345");
    }

    @Test(expected = NullPointerException.class)
    public void encode_withNullCharset_throwsNpe() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode("Hello", (Charset) null);
    }

    @Test(expected = NullPointerException.class)
    public void decode_withNullCharset_throwsNpe() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode("Hello", (Charset) null);
    }

    @Test
    public void encode_object_overload_null_returnsNull() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(true);
        assertNull(codec.encode((Object) null));
    }

    @Test(expected = EncoderException.class)
    public void encode_object_overload_wrongType_throwsEncoderException() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode(new Object()); // not a String or byte[]
    }

    @Test
    public void decode_object_overload_null_returnsNull() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(true);
        assertNull(codec.decode((Object) null));
    }

    @Test(expected = DecoderException.class)
    public void decode_object_overload_wrongType_throwsDecoderException() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(new Object()); // not a String or byte[]
    }

    // ---------------------------------------------------------------------
    // Static helpers: encodeQuotedPrintable / decodeQuotedPrintable
    // ---------------------------------------------------------------------

    @Test
    public void static_encodeQuotedPrintable_nulls_returnNull() {
        assertNull(QuotedPrintableCodec.encodeQuotedPrintable(null, (byte[]) null));
        assertNull(QuotedPrintableCodec.encodeQuotedPrintable(null, (byte[]) null, true));
    }

    @Test
    public void static_decodeQuotedPrintable_null_returnsNull() throws Exception {
        assertNull(QuotedPrintableCodec.decodeQuotedPrintable((byte[]) null));
    }

    @Test
    public void static_encodeQuotedPrintable_roundTrip_thenDecode() throws Exception {
        byte[] data = "Line with\ttab and space ".getBytes(StandardCharsets.UTF_8);
        byte[] qp = QuotedPrintableCodec.encodeQuotedPrintable((BitSet) null, data, true);
        assertNotNull(qp);
        byte[] decoded = QuotedPrintableCodec.decodeQuotedPrintable(qp);
        assertArrayEquals(data, decoded);
    }

    @Test
    public void static_encodeQuotedPrintable_emptyArray_returnsEmptyArray() {
        byte[] empty = new byte[0];
        byte[] qp = QuotedPrintableCodec.encodeQuotedPrintable(new BitSet(), empty);
        assertNotNull(qp);
        assertEquals(0, qp.length);
    }

    @Test
    public void static_encodeQuotedPrintable_minBytes_behavior_returnsNullForTooShort() {
        // The implementation documents a minimum length (MIN_BYTES = 3).
        // With strict=true and input shorter than MIN_BYTES, it should return null.
        byte[] tooShort = new byte[2];
        byte[] qp = QuotedPrintableCodec.encodeQuotedPrintable(null, tooShort, true);
        assertNull(qp);
    }
}