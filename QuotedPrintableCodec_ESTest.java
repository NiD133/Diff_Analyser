package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

/**
 * Test suite for QuotedPrintableCodec functionality.
 * Tests cover encoding, decoding, charset handling, and error conditions.
 */
public class QuotedPrintableCodecTest {

    // Test data constants
    private static final String UTF8_CHARSET = "UTF-8";
    private static final String SAMPLE_TEXT = "Hello World";
    private static final String TEXT_WITH_SPACE = "Hello World ";
    private static final String ENCODED_SPACE_SUFFIX = "Hello World=20";
    
    // ========== Constructor Tests ==========
    
    @Test
    public void testDefaultConstructor() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertEquals("Default charset should be UTF-8", UTF8_CHARSET, codec.getDefaultCharset());
        assertNotNull("Charset should not be null", codec.getCharset());
    }
    
    @Test
    public void testConstructorWithStrictMode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(true);
        assertEquals("Charset should be UTF-8 even in strict mode", UTF8_CHARSET, codec.getDefaultCharset());
    }
    
    @Test
    public void testConstructorWithCharset() {
        Charset charset = StandardCharsets.UTF_8;
        QuotedPrintableCodec codec = new QuotedPrintableCodec(charset);
        assertEquals("Should use provided charset", charset, codec.getCharset());
    }
    
    @Test
    public void testConstructorWithNullCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);
        assertNull("Charset should be null when explicitly set to null", codec.getCharset());
    }
    
    @Test
    public void testConstructorWithValidCharsetName() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-4");
        assertEquals("Should support ISO-8859-4 charset", "ISO-8859-4", codec.getCharset().name());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullCharsetName() {
        new QuotedPrintableCodec((String) null);
    }
    
    @Test(expected = IllegalCharsetNameException.class)
    public void testConstructorWithInvalidCharsetName() {
        new QuotedPrintableCodec("Invalid@Charset#Name");
    }
    
    @Test(expected = UnsupportedCharsetException.class)
    public void testConstructorWithUnsupportedCharset() {
        new QuotedPrintableCodec("NonExistentCharset");
    }

    // ========== Basic Encoding Tests ==========
    
    @Test
    public void testEncodeEmptyString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.encode("");
        assertEquals("Empty string should remain empty", "", result);
    }
    
    @Test
    public void testEncodeSimpleText() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.encode(SAMPLE_TEXT);
        assertEquals("Simple ASCII text should not be encoded", SAMPLE_TEXT, result);
    }
    
    @Test
    public void testEncodeTextWithTrailingSpace() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(true); // strict mode
        String result = codec.encode(TEXT_WITH_SPACE);
        assertEquals("Trailing space should be encoded in strict mode", ENCODED_SPACE_SUFFIX, result);
    }
    
    @Test
    public void testEncodeNullString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.encode((String) null);
        assertNull("Null input should return null", result);
    }
    
    @Test
    public void testEncodeEmptyByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = new byte[0];
        byte[] result = codec.encode(input);
        assertArrayEquals("Empty byte array should remain empty", input, result);
    }
    
    @Test
    public void testEncodeNullByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] result = codec.encode((byte[]) null);
        assertNull("Null byte array should return null", result);
    }

    // ========== Basic Decoding Tests ==========
    
    @Test
    public void testDecodeEmptyString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.decode("");
        assertEquals("Empty string should remain empty", "", result);
    }
    
    @Test
    public void testDecodeSimpleText() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.decode(SAMPLE_TEXT);
        assertEquals("Simple text should decode unchanged", SAMPLE_TEXT, result);
    }
    
    @Test
    public void testDecodeEncodedSpace() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.decode(ENCODED_SPACE_SUFFIX);
        assertEquals("=20 should decode to space", TEXT_WITH_SPACE, result);
    }
    
    @Test
    public void testDecodeNullString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.decode((String) null);
        assertNull("Null input should return null", result);
    }
    
    @Test
    public void testDecodeEmptyByteArray() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = new byte[0];
        byte[] result = codec.decode(input);
        assertNotNull("Result should not be null", result);
    }
    
    @Test
    public void testDecodeNullByteArray() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] result = codec.decode((byte[]) null);
        assertNull("Null byte array should return null", result);
    }

    // ========== Charset-Specific Tests ==========
    
    @Test
    public void testEncodeWithSpecificCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Charset charset = StandardCharsets.UTF_8;
        String result = codec.encode(SAMPLE_TEXT, charset);
        assertEquals("Should encode with specified charset", SAMPLE_TEXT, result);
    }
    
    @Test
    public void testDecodeWithSpecificCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Charset charset = StandardCharsets.UTF_8;
        String result = codec.decode(SAMPLE_TEXT, charset);
        assertEquals("Should decode with specified charset", SAMPLE_TEXT, result);
    }
    
    @Test
    public void testEncodeWithCharsetName() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.encode(SAMPLE_TEXT, UTF8_CHARSET);
        assertEquals("Should encode with charset name", SAMPLE_TEXT, result);
    }
    
    @Test
    public void testDecodeWithCharsetName() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.decode(SAMPLE_TEXT, UTF8_CHARSET);
        assertEquals("Should decode with charset name", SAMPLE_TEXT, result);
    }

    // ========== Object Encoding/Decoding Tests ==========
    
    @Test
    public void testEncodeStringObject() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object result = codec.encode((Object) SAMPLE_TEXT);
        assertEquals("Should encode string object", SAMPLE_TEXT, result);
    }
    
    @Test
    public void testDecodeStringObject() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object result = codec.decode((Object) SAMPLE_TEXT);
        assertEquals("Should decode string object", SAMPLE_TEXT, result);
    }
    
    @Test
    public void testEncodeNullObject() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object result = codec.encode((Object) null);
        assertNull("Null object should return null", result);
    }
    
    @Test
    public void testDecodeNullObject() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object result = codec.decode((Object) null);
        assertNull("Null object should return null", result);
    }

    // ========== Static Method Tests ==========
    
    @Test
    public void testStaticEncodeWithEmptyArray() {
        byte[] input = new byte[0];
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, input);
        assertArrayEquals("Empty array should remain empty", new byte[0], result);
    }
    
    @Test
    public void testStaticEncodeWithNullArray() {
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, null);
        assertNull("Null input should return null", result);
    }
    
    @Test
    public void testStaticEncodeWithStrictMode() {
        byte[] input = {0, 0, 0}; // null bytes that need encoding
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, input, true);
        assertNotNull("Result should not be null", result);
        assertTrue("Encoded result should be longer than input", result.length > input.length);
    }
    
    @Test
    public void testStaticDecodeWithEmptyArray() throws Exception {
        byte[] input = new byte[0];
        byte[] result = QuotedPrintableCodec.decodeQuotedPrintable(input);
        assertNotNull("Result should not be null", result);
    }
    
    @Test
    public void testStaticDecodeWithNullArray() throws Exception {
        byte[] result = QuotedPrintableCodec.decodeQuotedPrintable(null);
        assertNull("Null input should return null", result);
    }

    // ========== Error Condition Tests ==========
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testEncodeWithInvalidCharsetName() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode("test", "InvalidCharsetName");
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeWithInvalidCharsetName() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode("test", "InvalidCharsetName");
    }
    
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidQuotedPrintableSequence() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        // Invalid sequence: = followed by non-hex characters
        codec.decode("Invalid=GG");
    }
    
    @Test(expected = DecoderException.class)
    public void testDecodeIncompleteQuotedPrintableSequence() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        // Incomplete sequence: = at end without hex digits
        byte[] invalidInput = {61, 0}; // '=' followed by null byte
        codec.decode(invalidInput);
    }
    
    @Test(expected = EncoderException.class)
    public void testEncodeUnsupportedObjectType() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode((Object) codec); // Try to encode the codec itself
    }
    
    @Test(expected = DecoderException.class)
    public void testDecodeUnsupportedObjectType() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode((Object) codec); // Try to decode the codec itself
    }
    
    @Test(expected = NullPointerException.class)
    public void testEncodeWithNullCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);
        codec.encode("test");
    }
    
    @Test(expected = NullPointerException.class)
    public void testDecodeWithNullCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);
        codec.decode("test");
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetDefaultCharsetWithNullCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);
        codec.getDefaultCharset();
    }

    // ========== Special Character Tests ==========
    
    @Test
    public void testEncodeSpecialCharacters() {
        byte[] input = {9, 32, 13, 10}; // tab, space, CR, LF
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, input, true);
        assertNotNull("Result should not be null", result);
    }
    
    @Test
    public void testEncodeNonPrintableCharacters() {
        byte[] input = {(byte) 0xFF, (byte) 0x00, (byte) 0x80}; // non-printable bytes
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, input);
        assertNotNull("Result should not be null", result);
        assertTrue("Non-printable chars should increase encoded length", result.length > input.length);
    }
    
    @Test
    public void testRoundTripEncoding() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String original = "Test with special chars: àáâãäå";
        String encoded = codec.encode(original);
        String decoded = codec.decode(encoded);
        assertEquals("Round-trip encoding should preserve original", original, decoded);
    }

    // ========== BitSet Tests ==========
    
    @Test
    public void testEncodeWithCustomBitSet() {
        BitSet customPrintable = new BitSet();
        customPrintable.set('A');
        customPrintable.set('B');
        
        byte[] input = {'A', 'B', 'C'}; // Only A and B are "printable" in our custom set
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(customPrintable, input);
        
        assertNotNull("Result should not be null", result);
        // C should be encoded, A and B should not
        assertTrue("Result should be longer due to encoding C", result.length > input.length);
    }
    
    // ========== Integration Tests ==========
    
    @Test
    public void testComplexEncodingDecoding() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(true); // strict mode
        
        // Create test data with various byte values
        byte[] originalData = new byte[50];
        for (int i = 0; i < originalData.length; i++) {
            originalData[i] = (byte) (i * 5); // Mix of values
        }
        
        // Encode and then decode
        byte[] encoded = codec.encode(originalData);
        byte[] decoded = codec.decode(encoded);
        
        assertNotNull("Encoded data should not be null", encoded);
        assertNotNull("Decoded data should not be null", decoded);
        assertEquals("Decoded length should match original", originalData.length, decoded.length);
    }
}