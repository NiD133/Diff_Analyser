package org.apache.commons.codec.net;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * Readable, behavior-focused tests for BCodec.
 *
 * These tests avoid EvoSuite-specific scaffolding and use clear naming,
 * small Arrange-Act-Assert sections, and stable expectations.
 */
public class BCodecTest {

    // ---------------------------------------------------------------------
    // Basic metadata
    // ---------------------------------------------------------------------

    @Test
    public void getEncoding_returnsUppercaseB() {
        assertEquals("B", new BCodec().getEncoding());
    }

    // ---------------------------------------------------------------------
    // Constructors and configuration
    // ---------------------------------------------------------------------

    @Test
    public void defaultConstructor_isLenientDecoding() {
        assertFalse(new BCodec().isStrictDecoding());
    }

    @Test
    public void strictPolicyConstructor_reportsStrictDecoding() {
        BCodec codec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.STRICT);
        assertTrue(codec.isStrictDecoding());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_nullCharset_throwsNPE() {
        new BCodec((Charset) null);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_nullCharsetWithPolicy_throwsNPE() {
        new BCodec(null, CodecPolicy.STRICT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_nullCharsetName_throwsIAE() {
        new BCodec((String) null);
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void constructor_unsupportedCharsetName_throwsUCE() {
        new BCodec("p-Ubb"); // unknown charset
    }

    // ---------------------------------------------------------------------
    // Encoding: happy paths
    // ---------------------------------------------------------------------

    @Test
    public void encode_defaultCharset_usesUtf8InRfc1522Header() throws Exception {
        BCodec codec = new BCodec();
        String encoded = codec.encode("hello");
        assertTrue("Header should start with UTF-8 B encoding marker",
                encoded.startsWith("=?UTF-8?B?"));
    }

    @Test
    public void encode_withCharsetObject_encodesToExpectedHeader() throws Exception {
        BCodec codec = new BCodec();
        String encoded = codec.encode("^QOTD7,4PZ$(<r", StandardCharsets.UTF_8);
        assertEquals("=?UTF-8?B?XlFPVEQ3LDRQWiQoPHI=?=", encoded);
    }

    @Test
    public void encode_withCharsetName_utf8() throws Exception {
        BCodec codec = new BCodec();
        String encoded = codec.encode(" encoded content", "UTF-8");
        assertEquals("=?UTF-8?B?IGVuY29kZWQgY29udGVudA==?=", encoded);
    }

    @Test
    public void encode_objectString_returnsHeader() throws Exception {
        BCodec codec = new BCodec();
        Object encoded = codec.encode((Object) " encoded content");
        assertEquals("=?UTF-8?B?IGVuY29kZWQgY29udGVudA==?=", encoded);
    }

    // ---------------------------------------------------------------------
    // Encoding: null handling
    // ---------------------------------------------------------------------

    @Test
    public void encode_nullString_returnsNull() throws Exception {
        BCodec codec = new BCodec();
        assertNull(codec.encode((String) null));
    }

    @Test
    public void encode_nullStringWithCharsetObject_returnsNull() throws Exception {
        BCodec codec = new BCodec();
        assertNull(codec.encode(null, StandardCharsets.UTF_8));
    }

    @Test
    public void encode_nullStringWithCharsetName_returnsNull() throws Exception {
        BCodec codec = new BCodec();
        assertNull(codec.encode(null, "UTF-8"));
    }

    @Test
    public void encode_nullObject_returnsNull() throws Exception {
        BCodec codec = new BCodec();
        assertNull(codec.encode((Object) null));
    }

    // ---------------------------------------------------------------------
    // Encoding: invalid inputs
    // ---------------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void encode_withNullCharsetObject_throwsNPE() throws Exception {
        new BCodec().encode("text", (Charset) null);
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void encode_withEmptyCharsetName_throwsIllegalCharsetName() throws Exception {
        new BCodec().encode("text", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void encode_withNullCharsetName_throwsIAE() throws Exception {
        new BCodec().encode("text", (String) null);
    }

    @Test
    public void encode_objectOfUnsupportedType_throwsEncoderException() {
        BCodec codec = new BCodec();
        try {
            codec.encode(new Object());
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertTrue(e.getMessage().contains("cannot be encoded using BCodec"));
        }
    }

    // ---------------------------------------------------------------------
    // Decoding: happy paths
    // ---------------------------------------------------------------------

    @Test
    public void decode_rfc1522Header_returnsDecodedText() throws Exception {
        BCodec codec = new BCodec();
        String decoded = codec.decode("=?UTF-8?B?IGVuY29kZWQgY29udGVudA==?=");
        assertEquals(" encoded content", decoded);
    }

    @Test
    public void decode_objectHeader_returnsDecodedText() throws Exception {
        BCodec codec = new BCodec();
        Object decoded = codec.decode((Object) "=?UTF-8?B?LHcuLHAlKw==?=");
        assertEquals(",w.,p%+", decoded);
    }

    @Test
    public void decode_emptyBase64Payload_returnsEmptyString() throws Exception {
        BCodec codec = new BCodec();
        assertEquals("", codec.decode("=?UTF-8?B??="));
    }

    // ---------------------------------------------------------------------
    // Decoding: null handling
    // ---------------------------------------------------------------------

    @Test
    public void decode_nullString_returnsNull() throws Exception {
        BCodec codec = new BCodec();
        assertNull(codec.decode((String) null));
    }

    @Test
    public void decode_nullObject_returnsNull() throws Exception {
        BCodec codec = new BCodec();
        assertNull(codec.decode((Object) null));
    }

    // ---------------------------------------------------------------------
    // Decoding: invalid inputs
    // ---------------------------------------------------------------------

    @Test
    public void decode_invalidRfc1522Header_throwsDecoderException() {
        BCodec codec = new BCodec();
        try {
            codec.decode("WgJp7)");
            fail("Expected DecoderException");
        } catch (DecoderException e) {
            assertTrue(e.getMessage().contains("RFC 1522 violation"));
        }
    }

    @Test
    public void decode_objectOfUnsupportedType_throwsDecoderException() {
        BCodec codec = new BCodec();
        try {
            codec.decode(new Object());
            fail("Expected DecoderException");
        } catch (DecoderException e) {
            assertTrue(e.getMessage().contains("cannot be decoded using BCodec"));
        }
    }

    // ---------------------------------------------------------------------
    // Low-level byte[] helpers (protected methods): null passthrough and strict behavior
    // ---------------------------------------------------------------------

    @Test
    public void doEncoding_nullBytes_returnsNull() {
        BCodec codec = new BCodec();
        assertNull(codec.doEncoding(null));
    }

    @Test
    public void doDecoding_nullBytes_returnsNull() {
        BCodec codec = new BCodec();
        assertNull(codec.doDecoding(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void strictDecoding_rejectsInvalidBase64Bytes() {
        BCodec strict = new BCodec(StandardCharsets.UTF_8, CodecPolicy.STRICT);
        // Not valid Base64 characters; Base64 in STRICT mode should reject.
        strict.doDecoding(new byte[] { '-', '-', '-', '-' });
    }
}