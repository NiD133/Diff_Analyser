package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.binary.BinaryCodec;

/**
 * Test suite for BinaryCodec functionality.
 * BinaryCodec converts between binary data and ASCII binary string representations.
 */
public class BinaryCodecTest {

    // Test basic encoding functionality
    
    @Test
    public void testEncodeNullInput() {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.encode((byte[]) null);
        
        assertArrayEquals("Encoding null should return empty array", new byte[]{}, result);
    }
    
    @Test
    public void testEncodeSingleByte() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = {(byte) 64}; // Binary: 01000000
        byte[] encoded = codec.encode(input);
        
        assertEquals("Single byte should encode to 8 ASCII characters", 8, encoded.length);
    }
    
    @Test
    public void testEncodeMultipleBytes() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[17]; // 17 bytes
        byte[] encoded = codec.encode(input);
        
        assertEquals("17 bytes should encode to 136 ASCII characters (17 * 8)", 136, encoded.length);
    }
    
    // Test basic decoding functionality
    
    @Test
    public void testDecodeEmptyInput() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[1]; // Single zero byte
        byte[] decoded = codec.decode(input);
        
        assertEquals("Single zero byte should decode to empty array", 0, decoded.length);
    }
    
    @Test
    public void testDecodeEightZeroBits() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[8]; // Eight zero bytes representing "00000000"
        byte[] decoded = codec.decode(input);
        
        assertArrayEquals("Eight zero bits should decode to single zero byte", 
                         new byte[]{(byte) 0}, decoded);
    }
    
    // Test string conversion methods
    
    @Test
    public void testToByteArrayWithNullString() {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.toByteArray((String) null);
        
        assertEquals("Null string should return empty array", 0, result.length);
    }
    
    @Test
    public void testToByteArrayWithInvalidBinaryString() {
        BinaryCodec codec = new BinaryCodec();
        String invalidBinary = "\"S|Pn_%?u{!|"; // Contains non-binary characters
        byte[] result = codec.toByteArray(invalidBinary);
        
        assertArrayEquals("Invalid binary string should return single zero byte", 
                         new byte[]{(byte) 0}, result);
    }
    
    @Test
    public void testToByteArrayWithMixedCharacters() {
        BinaryCodec codec = new BinaryCodec();
        String mixed = "lb{1yPz"; // Mix of valid and invalid characters
        byte[] result = codec.toByteArray(mixed);
        
        assertEquals("Mixed character string should return empty result", 0, result.length);
    }
    
    // Test ASCII conversion utilities
    
    @Test
    public void testToAsciiStringFromSingleByte() {
        byte[] input = {(byte) 0};
        String result = BinaryCodec.toAsciiString(input);
        
        assertEquals("Single zero byte should convert to '00000000'", "00000000", result);
    }
    
    @Test
    public void testToAsciiCharsFromNullInput() {
        char[] result = BinaryCodec.toAsciiChars((byte[]) null);
        
        assertEquals("Null input should return empty char array", 0, result.length);
    }
    
    @Test
    public void testToAsciiCharsFromSingleByte() {
        byte[] input = {(byte) 64}; // Binary: 01000000
        char[] result = BinaryCodec.toAsciiChars(input);
        
        assertArrayEquals("Byte 64 should convert to '01000000' as char array",
                         new char[]{'0', '1', '0', '0', '0', '0', '0', '0'}, result);
    }
    
    @Test
    public void testToAsciiBytesFromNullInput() {
        byte[] result = BinaryCodec.toAsciiBytes((byte[]) null);
        
        assertArrayEquals("Null input should return empty byte array", new byte[]{}, result);
    }
    
    // Test round-trip conversion (encode then decode)
    
    @Test
    public void testRoundTripConversion() {
        byte[] original = {(byte) 48}; // ASCII '0'
        byte[] asciiBytes = BinaryCodec.toAsciiBytes(original);
        byte[] decoded = BinaryCodec.fromAscii(asciiBytes);
        
        assertArrayEquals("Round-trip conversion should preserve original data", 
                         new byte[]{(byte) 0, (byte) 0, (byte) 48}, decoded);
    }
    
    @Test
    public void testMultipleEncodingOperations() {
        BinaryCodec codec = new BinaryCodec();
        
        // Chain: null -> decode -> encode -> decode
        Object decoded1 = codec.decode((Object) null);
        Object encoded = codec.encode(decoded1);
        Object decoded2 = codec.decode(encoded);
        
        assertSame("Multiple operations on null should return same result", decoded2, decoded1);
    }
    
    // Test fromAscii methods
    
    @Test
    public void testFromAsciiWithNullCharArray() {
        byte[] result = BinaryCodec.fromAscii((char[]) null);
        
        assertEquals("Null char array should return empty byte array", 0, result.length);
    }
    
    @Test
    public void testFromAsciiWithEmptyCharArray() {
        char[] input = new char[0];
        byte[] result = BinaryCodec.fromAscii(input);
        
        assertEquals("Empty char array should return empty byte array", 0, result.length);
    }
    
    @Test
    public void testFromAsciiWithEmptyByteArray() {
        byte[] input = new byte[0];
        byte[] result = BinaryCodec.fromAscii(input);
        
        assertNotSame("Should return new array instance", result, input);
        assertEquals("Empty byte array should return empty result", 0, result.length);
    }
    
    @Test
    public void testFromAsciiWithInvalidCharacter() {
        byte[] input = new byte[9];
        input[1] = (byte) 102; // 'f' - not a valid binary digit
        byte[] result = BinaryCodec.fromAscii(input);
        
        assertArrayEquals("Invalid binary character should result in zero byte", 
                         new byte[]{(byte) 0}, result);
    }
    
    // Test utility methods
    
    @Test
    public void testIsEmptyWithNullArray() {
        boolean result = BinaryCodec.isEmpty((byte[]) null);
        
        assertTrue("Null array should be considered empty", result);
    }
    
    @Test
    public void testIsEmptyWithNonEmptyArray() {
        byte[] input = new byte[8];
        boolean result = BinaryCodec.isEmpty(input);
        
        assertFalse("Non-empty array should not be considered empty", result);
    }
    
    // Test error conditions
    
    @Test
    public void testEncodeWithInvalidObjectType() {
        BinaryCodec codec = new BinaryCodec();
        
        try {
            codec.encode((Object) codec);
            fail("Should throw exception for invalid object type");
        } catch (Exception e) {
            assertEquals("Should have specific error message", 
                        "argument not a byte array", e.getMessage());
        }
    }
    
    @Test
    public void testDecodeWithInvalidObjectType() {
        BinaryCodec codec = new BinaryCodec();
        
        try {
            codec.decode((Object) codec);
            fail("Should throw exception for invalid object type");
        } catch (Exception e) {
            assertEquals("Should have specific error message", 
                        "argument not a byte array", e.getMessage());
        }
    }
    
    @Test
    public void testDecodeWithStringInput() {
        BinaryCodec codec = new BinaryCodec();
        
        // This should work - string input is valid for decode
        Object result1 = codec.decode((Object) "(mj0N>],r1/P");
        Object result2 = codec.decode(result1);
        
        assertNotSame("Multiple decode operations should produce different objects", result2, result1);
    }
    
    // Test memory/performance edge cases
    
    @Test(expected = OutOfMemoryError.class)
    public void testExcessiveEncodingOperations() {
        // Test the pattern that causes exponential growth
        byte[] data = new byte[5];
        byte[] result1 = BinaryCodec.toAsciiBytes(data);      // 5 -> 40 bytes
        byte[] result2 = BinaryCodec.toAsciiBytes(result1);   // 40 -> 320 bytes  
        byte[] result3 = BinaryCodec.toAsciiBytes(result2);   // 320 -> 2560 bytes - should cause OutOfMemoryError
        
        BinaryCodec.toAsciiString(result3); // This should fail
    }
    
    @Test(expected = OutOfMemoryError.class) 
    public void testExcessiveToAsciiCharsOperations() {
        byte[] data = new byte[5];
        byte[] result1 = BinaryCodec.toAsciiBytes(data);
        byte[] result2 = BinaryCodec.toAsciiBytes(result1);
        byte[] result3 = BinaryCodec.toAsciiBytes(result2);
        
        BinaryCodec.toAsciiChars(result3); // Should cause OutOfMemoryError
    }
    
    @Test(expected = OutOfMemoryError.class)
    public void testExcessiveEncodingWithLargerInput() {
        byte[] data = new byte[16];
        byte[] result1 = BinaryCodec.toAsciiBytes(data);   // 16 -> 128 bytes
        byte[] result2 = BinaryCodec.toAsciiBytes(result1); // 128 -> 1024 bytes
        
        BinaryCodec.toAsciiBytes(result2); // Should cause OutOfMemoryError
    }
}