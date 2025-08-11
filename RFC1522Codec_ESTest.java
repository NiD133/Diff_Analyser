package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * Readable tests for RFC1522-compatible codec behavior through concrete implementations
 * BCodec (Base64) and QCodec (Quoted-Printable).
 *
 * Notes:
 * - This test lives in the same package to access protected encodeText/decodeText.
 * - We focus on clear behavior coverage and stable assertions.
 */
public class RFC1522CodecTest {

    // ---------------------------------------------------------------------
    // Null handling
    // ---------------------------------------------------------------------

    @Test
    public void encodeText_returnsNull_whenInputIsNull_withCharset() throws EncoderException {
        QCodec q = new QCodec();
        String encoded = q.encodeText(null, Charset.defaultCharset());
        assertNull(encoded);
    }

    @Test
    public void encodeText_returnsNull_whenInputIsNull_withCharsetName() throws EncoderException {
        BCodec b = new BCodec();
        // Even with a nonsense charset name, null input must short-circuit to null.
        String encoded = b.encodeText(null, "does-not-matter");
        assertNull(encoded);
    }

    @Test
    public void decodeText_returnsNull_whenInputIsNull() throws Exception {
        QCodec q = new QCodec();
        String decoded = q.decodeText(null);
        assertNull(decoded);
    }

    // ---------------------------------------------------------------------
    // Successful encodes
    // ---------------------------------------------------------------------

    @Test
    public void bCodec_encodesToExpectedBase64_withUtf8_charsetName() throws EncoderException {
        BCodec b = new BCodec();
        String encoded = b.encodeText("@~_=I", "UTF-8");
        assertNotNull(encoded);
        assertEquals("=?UTF-8?B?QH5fPUk=?=", encoded);
    }

    @Test
    public void bCodec_encodesToExpectedBase64_withExplicitCharset() throws EncoderException {
        BCodec b = new BCodec(Charset.forName("UTF-8"), CodecPolicy.STRICT);
        String input = "=?=?=?UTF-8?Q?Q?=";
        String encoded = b.encodeText(input, Charset.forName("UTF-8"));
        assertEquals("=?UTF-8?B?PT89Pz0/VVRGLTg/UT9RPz0=?=", encoded);
    }

    // ---------------------------------------------------------------------
    // Successful decodes
    // ---------------------------------------------------------------------

    @Test
    public void bCodec_decodesBase64PayloadSuccessfully() throws Exception {
        BCodec b = new BCodec();
        String decoded = b.decodeText("=?UTF-8?B?PT9eLT89Pz0=?=");
        assertEquals("=?^-?=?=", decoded);
    }

    @Test
    public void bCodec_decodesEmptyBase64PayloadToEmptyString() throws Exception {
        BCodec b = new BCodec();
        String decoded = b.decodeText("=?UTF-8?B??=");
        assertEquals("", decoded);
    }

    // ---------------------------------------------------------------------
    // Decode errors (RFC 1522 violations and related issues)
    // ---------------------------------------------------------------------

    @Test
    public void bCodec_decode_rejectsQEncodedContent() {
        BCodec b = new BCodec();
        DecoderException ex = assertThrows(DecoderException.class,
                () -> b.decodeText("=?UTF-8?Q?Q?=?="));
        // Implementation message is helpful:
        // "This codec cannot decode Q encoded content"
        // We do not assert the exact message to keep this test robust.
    }

    @Test
    public void bCodec_decode_failsWhenEncodingTokenMissing() {
        BCodec b = new BCodec();
        assertThrows(DecoderException.class, () -> b.decodeText("=?=?SRq9'.C?="));
    }

    @Test
    public void bCodec_decode_failsWhenCharsetNotSpecified() {
        BCodec b = new BCodec();
        assertThrows(DecoderException.class, () -> b.decodeText("=??-?="));
    }

    @Test
    public void bCodec_decode_failsWhenCharsetTokenMissing() {
        BCodec b = new BCodec();
        assertThrows(DecoderException.class, () -> b.decodeText("=?^-?="));
    }

    @Test
    public void bCodec_decode_failsOnMalformedEncodedContent() {
        BCodec b = new BCodec();
        assertThrows(DecoderException.class, () -> b.decodeText("=?=?=?ZCg05nk5fYK>>"));
    }

    @Test
    public void bCodec_decode_failsOnEmptyInput() {
        BCodec b = new BCodec();
        assertThrows(DecoderException.class, () -> b.decodeText(""));
    }

    @Test
    public void bCodec_decode_throwsUnsupportedEncoding_whenCharsetUnknown() {
        BCodec b = new BCodec();
        assertThrows(UnsupportedEncodingException.class, () -> b.decodeText("=?TF-?B??=?="));
    }

    // ---------------------------------------------------------------------
    // Encode errors (invalid or missing charset)
    // ---------------------------------------------------------------------

    @Test
    public void encodeText_throwsNullPointer_whenCharsetIsNull() {
        QCodec q = new QCodec();
        assertThrows(NullPointerException.class, () -> q.encodeText("{i", (Charset) null));
    }

    @Test
    public void encodeText_throwsUnsupportedCharset_whenNameUnknown() {
        BCodec b = new BCodec();
        assertThrows(UnsupportedCharsetException.class,
                () -> b.encodeText("payload", "org.apache.commons.codec.net.RFC1522Codec"));
    }

    @Test
    public void encodeText_throwsIllegalCharsetName_whenNameIllegal() {
        BCodec b = new BCodec();
        assertThrows(IllegalCharsetNameException.class,
                () -> b.encodeText("payload", "This codec cannot decode "));
    }

    @Test
    public void encodeText_throwsIllegalArgument_whenCharsetNameIsNull() {
        BCodec b = new BCodec();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> b.encodeText("payload", (String) null));
        // The JDK typically uses "Null charset name"
        // We avoid asserting the exact message for portability.
    }

    // ---------------------------------------------------------------------
    // Defaults
    // ---------------------------------------------------------------------

    @Test
    public void defaultCharset_isUtf8() {
        BCodec b = new BCodec();
        assertEquals("UTF-8", b.getDefaultCharset());
    }
}