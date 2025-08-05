package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.codec.net.URLCodec;

/**
 * Test suite for URLCodec functionality including encoding, decoding,
 * charset handling, and error conditions.
 */
public class URLCodec_ESTest {

    // ========== Encoding Tests ==========
    
    @Test
    public void testEncodeUrl_WithBitSetAndByteArray_ShouldEncodeCorrectly() throws Throwable {
        // Given: byte array with ASCII 'A' (65) and a BitSet
        byte[] input = new byte[3];
        input[0] = (byte) 65; // 'A'
        BitSet bitSet = BitSet.valueOf(input);
        
        // When: encoding the byte array
        byte[] result = URLCodec.encodeUrl(bitSet, input);
        
        // Then: should produce URL-encoded result
        assertArrayEquals(new byte[]{(byte) 37, (byte) 52, (byte) 49, (byte) 0, (byte) 0}, result);
    }

    @Test
    public void testEncode_WithNegativeByteValue_ShouldEncodeAndDecodeCorrectly() throws Throwable {
        // Given: URLCodec and byte array with negative value
        URLCodec codec = new URLCodec();
        byte[] input = new byte[7];
        input[2] = (byte) (-63);
        
        // When: encoding then decoding
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);
        
        // Then: should preserve original data
        assertArrayEquals(new byte[]{(byte) 0, (byte) 0, (byte) (-63), (byte) 0, (byte) 0, (byte) 0, (byte) 0}, decoded);
        assertEquals(7, decoded.length);
        assertEquals(21, encoded.length);
    }

    @Test
    public void testEncodeString_WithSpecialCharacters_ShouldEscapeProperly() throws Throwable {
        // Given: URLCodec and string with semicolon
        URLCodec codec = new URLCodec();
        
        // When: encoding string with special character
        String result = codec.encode("VoM;", "UTF-8");
        
        // Then: semicolon should be percent-encoded
        assertEquals("VoM%3B", result);
    }

    @Test
    public void testEncodeEmptyString_ShouldReturnEmptyString() throws Throwable {
        // Given: URLCodec and empty string
        URLCodec codec = new URLCodec();
        
        // When: encoding empty string
        String result = codec.encode("");
        
        // Then: should return empty string
        assertEquals("", result);
    }

    @Test
    public void testEncodeObject_WithStringContainingSpaces_ShouldReplacePlusSign() throws Throwable {
        // Given: URLCodec and string with spaces
        URLCodec codec = new URLCodec();
        
        // When: encoding object with spaces
        Object result = codec.encode((Object) " cannot be URL decoded");
        
        // Then: spaces should be replaced with plus signs
        assertEquals("+cannot+be+URL+decoded", result);
    }

    // ========== Decoding Tests ==========

    @Test
    public void testDecodeString_WithPlusSign_ShouldConvertToSpace() throws Throwable {
        // Given: URLCodec and string with plus sign
        URLCodec codec = new URLCodec();
        
        // When: decoding plus sign
        String result = codec.decode("+");
        
        // Then: should convert to space
        assertEquals(" ", result);
    }

    @Test
    public void testDecodeString_WithNormalText_ShouldReturnUnchanged() throws Throwable {
        // Given: URLCodec and normal string
        URLCodec codec = new URLCodec();
        
        // When: decoding normal string
        String result = codec.decode("UTF-8", "UTF-8");
        
        // Then: should return unchanged
        assertEquals("UTF-8", result);
    }

    @Test
    public void testDecodeObject_WithString_ShouldDecodeCorrectly() throws Throwable {
        // Given: URLCodec and string object
        URLCodec codec = new URLCodec();
        
        // When: decoding string object
        Object result = codec.decode((Object) "UTF-8");
        
        // Then: should return decoded string
        assertEquals("UTF-8", result);
    }

    @Test
    public void testDecodeUrl_WithPlusSignByte_ShouldConvertToSpace() throws Throwable {
        // Given: byte array with plus sign
        byte[] input = new byte[8];
        input[0] = (byte) 43; // '+'
        
        // When: decoding URL bytes
        byte[] decoded = URLCodec.decodeUrl(input);
        byte[] reencoded = URLCodec.encodeUrl(URLCodec.WWW_FORM_URL, decoded);
        
        // Then: plus should be converted to space (32)
        assertArrayEquals(new byte[]{(byte) 32, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, decoded);
        assertEquals(22, reencoded.length);
    }

    // ========== Charset and Configuration Tests ==========

    @Test
    public void testGetEncoding_WithDefaultCodec_ShouldReturnUTF8() throws Throwable {
        // Given: default URLCodec
        URLCodec codec = new URLCodec();
        
        // When: getting encoding
        String encoding = codec.getEncoding();
        
        // Then: should return UTF-8
        assertEquals("UTF-8", encoding);
    }

    @Test
    public void testGetEncoding_WithNullCharset_ShouldReturnNull() throws Throwable {
        // Given: URLCodec with null charset
        URLCodec codec = new URLCodec();
        codec.charset = null;
        
        // When: getting encoding
        String encoding = codec.getEncoding();
        
        // Then: should return null
        assertNull(encoding);
    }

    @Test
    public void testGetDefaultCharset_WithCustomCharset_ShouldReturnCustom() throws Throwable {
        // Given: URLCodec with empty string charset
        URLCodec codec = new URLCodec("");
        
        // When: getting default charset
        String charset = codec.getDefaultCharset();
        
        // Then: should return empty string
        assertEquals("", charset);
    }

    @Test
    public void testGetDefaultCharset_WithNullCharset_ShouldReturnNull() throws Throwable {
        // Given: URLCodec with null charset
        URLCodec codec = new URLCodec((String) null);
        
        // When: getting default charset
        String charset = codec.getDefaultCharset();
        
        // Then: should return null
        assertNull(charset);
    }

    // ========== Null Handling Tests ==========

    @Test
    public void testEncodeNull_ShouldReturnNull() throws Throwable {
        // Given: URLCodec
        URLCodec codec = new URLCodec();
        
        // When: encoding null values
        String nullStringResult = codec.encode((String) null);
        Object nullObjectResult = codec.encode((Object) null);
        byte[] nullByteResult = codec.encode((byte[]) null);
        
        // Then: should return null for all
        assertNull(nullStringResult);
        assertNull(nullObjectResult);
        assertNull(nullByteResult);
    }

    @Test
    public void testDecodeNull_ShouldReturnNull() throws Throwable {
        // Given: URLCodec
        URLCodec codec = new URLCodec();
        
        // When: decoding null values
        String nullStringResult = codec.decode((String) null);
        Object nullObjectResult = codec.decode((Object) null);
        byte[] nullByteResult = codec.decode((byte[]) null);
        
        // Then: should return null for all
        assertNull(nullStringResult);
        assertNull(nullObjectResult);
        assertNull(nullByteResult);
    }

    @Test
    public void testStaticMethods_WithNull_ShouldReturnNull() throws Throwable {
        // When: calling static methods with null
        byte[] encodeResult = URLCodec.encodeUrl(URLCodec.WWW_FORM_URL, null);
        byte[] decodeResult = URLCodec.decodeUrl(null);
        
        // Then: should return null
        assertNull(encodeResult);
        assertNull(decodeResult);
    }

    // ========== Empty Input Tests ==========

    @Test
    public void testEncodeEmptyByteArray_ShouldReturnEmptyArray() throws Throwable {
        // Given: URLCodec and empty byte array
        URLCodec codec = new URLCodec();
        byte[] emptyArray = new byte[0];
        
        // When: encoding empty array
        byte[] result = codec.encode(emptyArray);
        
        // Then: should return empty array
        assertEquals(0, result.length);
    }

    @Test
    public void testDecodeEmptyByteArray_ShouldReturnNewEmptyArray() throws Throwable {
        // Given: URLCodec and empty byte array
        URLCodec codec = new URLCodec();
        byte[] emptyArray = new byte[0];
        
        // When: decoding empty array
        byte[] result = codec.decode(emptyArray);
        
        // Then: should return new empty array (not same instance)
        assertNotSame(emptyArray, result);
    }

    @Test
    public void testStaticEncodeUrl_WithEmptyArray_ShouldReturnDifferentInstance() throws Throwable {
        // Given: empty byte array and BitSet
        byte[] emptyArray = new byte[0];
        BitSet bitSet = BitSet.valueOf(emptyArray);
        
        // When: encoding with static method
        byte[] result = URLCodec.encodeUrl(bitSet, emptyArray);
        
        // Then: should return different instance
        assertNotSame(result, emptyArray);
    }

    // ========== Error Condition Tests ==========

    @Test(expected = Exception.class)
    public void testDecode_WithInvalidUrlEncoding_ShouldThrowException() throws Throwable {
        // Given: URLCodec with custom charset and invalid URL string
        URLCodec codec = new URLCodec("`=G;6CyMxaWcZUTj%C");
        
        // When: decoding invalid URL encoding
        // Then: should throw exception
        codec.decode("`=G;6CyMxaWcZUTj%C");
    }

    @Test(expected = Exception.class)
    public void testDecode_WithEmptyCharset_ShouldThrowException() throws Throwable {
        // Given: URLCodec with empty charset
        URLCodec codec = new URLCodec("");
        
        // When: decoding with empty charset
        // Then: should throw exception
        codec.decode("");
    }

    @Test(expected = NullPointerException.class)
    public void testEncode_WithNullCharset_ShouldThrowNPE() throws Throwable {
        // Given: URLCodec with null charset
        URLCodec codec = new URLCodec((String) null);
        
        // When: encoding with null charset
        // Then: should throw NullPointerException
        codec.encode("");
    }

    @Test(expected = Exception.class)
    public void testEncode_WithUnsupportedObject_ShouldThrowException() throws Throwable {
        // Given: URLCodec and unsupported object type
        URLCodec codec = new URLCodec();
        
        // When: encoding URLCodec object itself
        // Then: should throw exception
        codec.encode((Object) codec);
    }

    @Test(expected = Exception.class)
    public void testDecode_WithUnsupportedObject_ShouldThrowException() throws Throwable {
        // Given: URLCodec and unsupported object type
        URLCodec codec = new URLCodec("3X{`l{LuA3.3r K44");
        Object unsupportedObject = new Object();
        
        // When: decoding unsupported object
        // Then: should throw exception
        codec.decode(unsupportedObject);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testEncode_WithInvalidCharset_ShouldThrowUnsupportedEncodingException() throws Throwable {
        // Given: URLCodec with invalid charset
        URLCodec codec = new URLCodec("org.apache.commons.codec.CharEncoding");
        
        // When: encoding with invalid charset
        // Then: should throw UnsupportedEncodingException
        codec.encode("org.apache.commons.codec.CharEncoding", "org.apache.commons.codec.CharEncoding");
    }

    @Test(expected = Exception.class)
    public void testDecodeUrl_WithIncompletePercentEncoding_ShouldThrowException() throws Throwable {
        // Given: byte array with incomplete percent encoding
        byte[] invalidEncoding = new byte[1];
        invalidEncoding[0] = (byte) 37; // '%' without following hex digits
        
        // When: decoding incomplete percent encoding
        // Then: should throw exception
        URLCodec.decodeUrl(invalidEncoding);
    }

    // ========== Special Cases Tests ==========

    @Test
    public void testEncodeUrl_WithNullBitSet_ShouldEncodeAllBytes() throws Throwable {
        // Given: byte array and null BitSet
        byte[] input = new byte[9];
        
        // When: encoding with null BitSet
        byte[] result = URLCodec.encodeUrl(null, input);
        
        // Then: should encode all bytes (3 chars per byte: %XX)
        assertEquals(27, result.length);
    }

    @Test
    public void testEncodeUrl_WithNegativeByte_ShouldEncodeCorrectly() throws Throwable {
        // Given: byte array with negative byte value
        byte[] input = new byte[8];
        input[3] = (byte) (-93);
        BitSet bitSet = URLCodec.WWW_FORM_URL;
        
        // When: encoding with WWW_FORM_URL BitSet
        byte[] result = URLCodec.encodeUrl(bitSet, input);
        
        // Then: should produce correctly sized result
        assertEquals(24, result.length);
    }

    @Test
    public void testEncode_WithValidString_ShouldReturnUnchanged() throws Throwable {
        // Given: URLCodec and string with only safe characters
        URLCodec codec = new URLCodec();
        String safeString = "org.apache.commons.codec.DecoderException";
        
        // When: encoding safe string
        String result = codec.encode(safeString);
        
        // Then: should return unchanged
        assertEquals("org.apache.commons.codec.DecoderException", result);
    }
}