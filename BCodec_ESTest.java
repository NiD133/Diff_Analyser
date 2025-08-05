package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.BCodec;

/**
 * Test suite for BCodec - Base64 encoding/decoding with RFC 1522 compliance.
 * Tests cover constructor variations, encoding/decoding operations, error handling,
 * and policy enforcement.
 */
public class BCodecTest {

    // Test Data Constants
    private static final String SAMPLE_TEXT = "^QOTD7,4PZ$(<r";
    private static final String EXPECTED_ENCODED_UTF8 = "=?UTF-8?B?XlFPVEQ3LDRQWiQoPHI=?=";
    private static final String ENCODED_CONTENT = " encoded content";
    private static final String EXPECTED_ENCODED_CONTENT = "=?UTF-8?B?IGVuY29kZWQgY29udGVudA==?=";
    private static final String DECODED_CONTENT = "=?UTF-8?B?IGVuY29kZWQgY29udGVudA==?=";
    private static final String EMPTY_ENCODED = "=?UTF-8?B??=";
    private static final String MALFORMED_ENCODED = "WgJp7)";
    private static final String INVALID_CHARSET = "p-Ubb";
    private static final String ILLEGAL_CHARSET_NAME = "encodeTable must have exactly 64 entries.";

    // ========== Constructor Tests ==========

    @Test
    public void testDefaultConstructor() {
        BCodec codec = new BCodec();
        assertEquals("Default encoding should be 'B'", "B", codec.getEncoding());
        assertFalse("Default decoding should be lenient", codec.isStrictDecoding());
    }

    @Test
    public void testConstructorWithCharset() {
        Charset charset = StandardCharsets.UTF_8;
        BCodec codec = new BCodec(charset);
        assertFalse("Decoding should be lenient by default", codec.isStrictDecoding());
    }

    @Test
    public void testConstructorWithCharsetAndStrictPolicy() {
        Charset charset = StandardCharsets.UTF_8;
        CodecPolicy strictPolicy = CodecPolicy.STRICT;
        BCodec codec = new BCodec(charset, strictPolicy);
        assertTrue("Should use strict decoding policy", codec.isStrictDecoding());
    }

    @Test
    public void testConstructorWithValidCharsetName() {
        // ISO-8859-15 is mapped from "l9" in some systems
        BCodec codec = new BCodec("l9");
        String result = codec.encode("l9");
        assertEquals("Should encode with specified charset", "=?ISO-8859-15?B?bDk=?=", result);
    }

    // ========== Constructor Error Handling Tests ==========

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullCharset() {
        new BCodec((Charset) null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullCharsetAndPolicy() {
        new BCodec(null, CodecPolicy.STRICT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullCharsetName() {
        new BCodec((String) null);
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testConstructorWithIllegalCharsetName() {
        new BCodec(ILLEGAL_CHARSET_NAME);
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void testConstructorWithUnsupportedCharset() {
        new BCodec(INVALID_CHARSET);
    }

    // ========== String Encoding Tests ==========

    @Test
    public void testEncodeStringWithDefaultCharset() {
        BCodec codec = new BCodec();
        String result = codec.encode(SAMPLE_TEXT);
        assertEquals("Should encode text with UTF-8", EXPECTED_ENCODED_UTF8, result);
    }

    @Test
    public void testEncodeStringWithSpecifiedCharset() {
        BCodec codec = new BCodec();
        Charset charset = StandardCharsets.UTF_8;
        String result = codec.encode(SAMPLE_TEXT, charset);
        assertEquals("Should encode with specified charset", EXPECTED_ENCODED_UTF8, result);
    }

    @Test
    public void testEncodeStringWithCharsetName() {
        BCodec codec = new BCodec("l9");
        String result = codec.encode("l9", "l9");
        assertEquals("Should encode with charset name", "=?ISO-8859-15?B?bDk=?=", result);
    }

    @Test
    public void testEncodeNullString() {
        BCodec codec = new BCodec();
        assertNull("Encoding null should return null", codec.encode((String) null));
    }

    @Test
    public void testEncodeNullStringWithCharset() {
        BCodec codec = new BCodec();
        Charset charset = StandardCharsets.UTF_8;
        assertNull("Encoding null with charset should return null", 
                  codec.encode(null, charset));
    }

    @Test
    public void testEncodeNullStringWithCharsetName() {
        BCodec codec = new BCodec();
        assertNull("Encoding null with charset name should return null", 
                  codec.encode(null, (String) null));
    }

    // ========== String Encoding Error Handling Tests ==========

    @Test(expected = EncoderException.class)
    public void testEncodeWithInvalidCharsetName() throws Exception {
        BCodec codec = new BCodec();
        codec.encode("test", "B"); // "B" is not a valid charset name
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testEncodeWithEmptyCharsetName() throws Exception {
        BCodec codec = new BCodec();
        codec.encode("test", ""); // Empty string is illegal charset name
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeWithNullCharsetName() throws Exception {
        BCodec codec = new BCodec();
        codec.encode(ENCODED_CONTENT, (String) null);
    }

    @Test(expected = NullPointerException.class)
    public void testEncodeWithNullCharset() throws Exception {
        BCodec codec = new BCodec();
        codec.encode("test", (Charset) null);
    }

    // ========== Object Encoding Tests ==========

    @Test
    public void testEncodeObjectString() throws Exception {
        BCodec codec = new BCodec();
        Object result = codec.encode((Object) ENCODED_CONTENT);
        assertEquals("Should encode string object", EXPECTED_ENCODED_CONTENT, result);
    }

    @Test
    public void testEncodeNullObject() throws Exception {
        BCodec codec = new BCodec();
        assertNull("Encoding null object should return null", codec.encode((Object) null));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidObjectType() throws Exception {
        BCodec codec = new BCodec();
        codec.encode((Object) codec); // BCodec object cannot be encoded
    }

    // ========== String Decoding Tests ==========

    @Test
    public void testDecodeValidEncodedString() throws Exception {
        BCodec codec = new BCodec();
        String result = codec.decode(DECODED_CONTENT);
        assertEquals("Should decode to original content", ENCODED_CONTENT, result);
    }

    @Test
    public void testDecodeEmptyEncodedString() throws Exception {
        BCodec codec = new BCodec();
        String result = codec.decode(EMPTY_ENCODED);
        assertEquals("Should decode empty content", "", result);
    }

    @Test
    public void testDecodeNullString() throws Exception {
        BCodec codec = new BCodec();
        assertNull("Decoding null should return null", codec.decode((String) null));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeMalformedString() throws Exception {
        BCodec codec = new BCodec();
        codec.decode(MALFORMED_ENCODED); // Not RFC 1522 compliant
    }

    // ========== Object Decoding Tests ==========

    @Test
    public void testDecodeObjectString() throws Exception {
        BCodec codec = new BCodec();
        Object result = codec.decode((Object) "=?UTF-8?B?LHcuLHAlKw==?=");
        assertEquals("Should decode string object", ",w.,p%+", result);
    }

    @Test
    public void testDecodeNullObject() throws Exception {
        BCodec codec = new BCodec();
        assertNull("Decoding null object should return null", codec.decode((Object) null));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidObjectType() throws Exception {
        BCodec codec = new BCodec();
        codec.decode((Object) codec); // BCodec object cannot be decoded
    }

    // ========== Byte Array Processing Tests ==========

    @Test
    public void testDoEncodingWithByteArray() {
        BCodec codec = new BCodec();
        byte[] input = new byte[7]; // All zeros
        byte[] result = codec.doEncoding(input);
        assertEquals("Encoded byte array should have expected length", 12, result.length);
    }

    @Test
    public void testDoEncodingWithNullByteArray() {
        BCodec codec = new BCodec();
        assertNull("Encoding null byte array should return null", 
                  codec.doEncoding(null));
    }

    @Test
    public void testDoDecodingWithByteArray() {
        BCodec codec = new BCodec();
        byte[] input = new byte[7];
        input[0] = (byte) 52; // '4'
        input[6] = (byte) 75; // 'K'
        byte[] result = codec.doDecoding(input);
        assertEquals("Decoded byte array should have expected length", 1, result.length);
    }

    @Test
    public void testDoDecodingWithNullByteArray() {
        BCodec codec = new BCodec();
        assertNull("Decoding null byte array should return null", 
                  codec.doDecoding(null));
    }

    @Test
    public void testEncodingDecodingRoundTrip() {
        BCodec codec = new BCodec();
        byte[] original = new byte[6];
        byte[] decoded = codec.doDecoding(original);
        byte[] reencoded = codec.doEncoding(decoded);
        assertNotSame("Round trip should produce different array instance", reencoded, original);
    }

    // ========== Strict Decoding Policy Tests ==========

    @Test(expected = NullPointerException.class)
    public void testStrictDecodingWithNullPolicy() {
        Charset charset = StandardCharsets.UTF_8;
        BCodec codec = new BCodec(charset, null);
        byte[] invalidData = new byte[22];
        codec.doDecoding(invalidData); // Should fail due to null policy
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStrictDecodingWithInvalidData() {
        Charset charset = StandardCharsets.UTF_8;
        CodecPolicy strictPolicy = CodecPolicy.STRICT;
        BCodec codec = new BCodec(charset, strictPolicy);
        
        byte[] invalidData = new byte[22];
        invalidData[3] = (byte) 45; // Invalid base64 character placement
        codec.doDecoding(invalidData); // Should fail in strict mode
    }

    // ========== Policy Configuration Tests ==========

    @Test
    public void testLenientDecodingPolicy() {
        BCodec codec = new BCodec();
        assertFalse("Default codec should use lenient decoding", codec.isStrictDecoding());
    }

    @Test
    public void testStrictDecodingPolicy() {
        Charset charset = StandardCharsets.UTF_8;
        CodecPolicy strictPolicy = CodecPolicy.STRICT;
        BCodec codec = new BCodec(charset, strictPolicy);
        assertTrue("Codec should use strict decoding when configured", codec.isStrictDecoding());
    }

    @Test
    public void testGetEncodingIdentifier() {
        BCodec codec = new BCodec();
        assertEquals("Encoding identifier should be 'B'", "B", codec.getEncoding());
    }
}