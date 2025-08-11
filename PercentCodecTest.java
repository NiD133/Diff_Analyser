package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the PercentCodec class, which implements Percent-Encoding.
 */
class PercentCodecTest {

    /**
     * Tests basic encoding and decoding functionality with a simple string.
     */
    @Test
    void testBasicEncodeDecode() throws Exception {
        final PercentCodec percentCodec = new PercentCodec();
        final String input = "abcdABCD";
        
        // Encode the input string
        final byte[] encodedBytes = percentCodec.encode(input.getBytes(StandardCharsets.UTF_8));
        final String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);
        
        // Decode the encoded string
        final byte[] decodedBytes = percentCodec.decode(encodedBytes);
        final String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        
        // Verify that encoding and decoding results match the original input
        assertEquals(input, encodedString, "Encoded string should match the input");
        assertEquals(input, decodedString, "Decoded string should match the input");
    }

    /**
     * Tests encoding of a space character.
     * This test is currently disabled.
     */
    @Test
    @Disabled("This test is disabled and might be removed in the future.")
    void testBasicSpace() throws Exception {
        final PercentCodec percentCodec = new PercentCodec();
        final String input = " ";
        
        // Encode the space character
        final byte[] encodedBytes = percentCodec.encode(input.getBytes(StandardCharsets.UTF_8));
        
        // Verify that the space is encoded as "%20"
        assertArrayEquals("%20".getBytes(StandardCharsets.UTF_8), encodedBytes);
    }

    /**
     * Tests configurable encoding with specific characters always encoded.
     */
    @Test
    void testConfigurablePercentEncoder() throws Exception {
        final String input = "abc123_-.*\u03B1\u03B2";
        final PercentCodec percentCodec = new PercentCodec("abcdef".getBytes(StandardCharsets.UTF_8), false);
        
        // Encode the input string
        final byte[] encodedBytes = percentCodec.encode(input.getBytes(StandardCharsets.UTF_8));
        final String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);
        
        // Verify the encoded result
        assertEquals("%61%62%63123_-.*%CE%B1%CE%B2", encodedString, "Encoded string should match expected output");
        
        // Decode the encoded string
        final byte[] decodedBytes = percentCodec.decode(encodedBytes);
        
        // Verify that decoding restores the original input
        assertEquals(input, new String(decodedBytes, StandardCharsets.UTF_8), "Decoded string should match the input");
    }

    /**
     * Tests decoding of an invalid encoded result, expecting a DecoderException.
     */
    @Test
    void testDecodeInvalidEncodedResultDecoding() throws Exception {
        final String input = "\u03B1\u03B2";
        final PercentCodec percentCodec = new PercentCodec();
        final byte[] encodedBytes = percentCodec.encode(input.getBytes(StandardCharsets.UTF_8));
        
        // Attempt to decode an invalid encoded result
        assertThrows(DecoderException.class, () -> {
            percentCodec.decode(Arrays.copyOf(encodedBytes, encodedBytes.length - 1)); // Exclude one byte
        });
    }

    /**
     * Tests decoding a null object, expecting a null result.
     */
    @Test
    void testDecodeNullObject() throws Exception {
        final PercentCodec percentCodec = new PercentCodec();
        assertNull(percentCodec.decode((Object) null), "Decoding null should return null");
    }

    /**
     * Tests decoding an unsupported object type, expecting a DecoderException.
     */
    @Test
    void testDecodeUnsupportedObject() {
        final PercentCodec percentCodec = new PercentCodec();
        assertThrows(DecoderException.class, () -> percentCodec.decode("test"), "Decoding unsupported object should throw DecoderException");
    }

    /**
     * Tests encoding a null object, expecting a null result.
     */
    @Test
    void testEncodeNullObject() throws Exception {
        final PercentCodec percentCodec = new PercentCodec();
        assertNull(percentCodec.encode((Object) null), "Encoding null should return null");
    }

    /**
     * Tests encoding an unsupported object type, expecting an EncoderException.
     */
    @Test
    void testEncodeUnsupportedObject() {
        final PercentCodec percentCodec = new PercentCodec();
        assertThrows(EncoderException.class, () -> percentCodec.encode("test"), "Encoding unsupported object should throw EncoderException");
    }

    /**
     * Tests initialization with invalid byte values, expecting an IllegalArgumentException.
     */
    @Test
    void testInvalidByte() throws Exception {
        final byte[] invalidBytes = { (byte) -1, (byte) 'A' };
        assertThrows(IllegalArgumentException.class, () -> new PercentCodec(invalidBytes, true), "Invalid byte values should throw IllegalArgumentException");
    }

    /**
     * Tests encoding and decoding with null or empty input.
     */
    @Test
    void testPercentEncoderDecoderWithNullOrEmptyInput() throws Exception {
        final PercentCodec percentCodec = new PercentCodec(null, true);
        
        // Test null input
        assertNull(percentCodec.encode(null), "Encoding null should return null");
        assertNull(percentCodec.decode(null), "Decoding null should return null");
        
        // Test empty input
        final byte[] emptyInput = "".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(emptyInput, percentCodec.encode(emptyInput), "Encoding empty input should return empty");
        assertArrayEquals(emptyInput, percentCodec.decode(emptyInput), "Decoding empty input should return empty");
    }

    /**
     * Tests encoding and decoding with spaces encoded as plus signs.
     */
    @Test
    void testPercentEncoderDecoderWithPlusForSpace() throws Exception {
        final String input = "a b c d";
        final PercentCodec percentCodec = new PercentCodec(null, true);
        
        // Encode the input string
        final byte[] encodedBytes = percentCodec.encode(input.getBytes(StandardCharsets.UTF_8));
        final String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);
        
        // Verify that spaces are encoded as '+'
        assertEquals("a+b+c+d", encodedString, "Spaces should be encoded as '+'");
        
        // Decode the encoded string
        final byte[] decodedBytes = percentCodec.decode(encodedBytes);
        
        // Verify that decoding restores the original input
        assertEquals(input, new String(decodedBytes, StandardCharsets.UTF_8), "Decoded string should match the input");
    }

    /**
     * Tests safe character encoding and decoding using Object type.
     */
    @Test
    void testSafeCharEncodeDecodeObject() throws Exception {
        final PercentCodec percentCodec = new PercentCodec(null, true);
        final String input = "abc123_-.*";
        
        // Encode the input string as an Object
        final Object encodedObject = percentCodec.encode((Object) input.getBytes(StandardCharsets.UTF_8));
        final String encodedString = new String((byte[]) encodedObject, StandardCharsets.UTF_8);
        
        // Decode the encoded Object
        final Object decodedObject = percentCodec.decode(encodedObject);
        final String decodedString = new String((byte[]) decodedObject, StandardCharsets.UTF_8);
        
        // Verify that encoding and decoding results match the original input
        assertEquals(input, encodedString, "Encoded string should match the input");
        assertEquals(input, decodedString, "Decoded string should match the input");
    }

    /**
     * Tests unsafe character encoding and decoding.
     */
    @Test
    void testUnsafeCharEncodeDecode() throws Exception {
        final PercentCodec percentCodec = new PercentCodec();
        final String input = "\u03B1\u03B2\u03B3\u03B4\u03B5\u03B6% ";
        
        // Encode the input string
        final byte[] encodedBytes = percentCodec.encode(input.getBytes(StandardCharsets.UTF_8));
        final String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);
        
        // Decode the encoded string
        final byte[] decodedBytes = percentCodec.decode(encodedBytes);
        final String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        
        // Verify that encoding and decoding results match the original input
        assertEquals("%CE%B1%CE%B2%CE%B3%CE%B4%CE%B5%CE%B6%25 ", encodedString, "Encoded string should match expected output");
        assertEquals(input, decodedString, "Decoded string should match the input");
    }
}