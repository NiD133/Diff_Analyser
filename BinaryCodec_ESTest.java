/*
 * Refactored for improved understandability and maintainability.
 * Changes include descriptive test names, explanatory comments, and standardized exception handling.
 */
package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.binary.BinaryCodec;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class BinaryCodec_ESTest extends BinaryCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testFromAsciiWithNonZeroByte() throws Throwable {
        // Test handling of non-zero byte in input array
        byte[] input = new byte[9];
        input[1] = (byte) 102; // Non-zero ASCII 'f'
        byte[] result = BinaryCodec.fromAscii(input);
        assertArrayEquals(new byte[]{(byte) 0}, result);
    }

    @Test(timeout = 4000)
    public void testToByteArrayFromInvalidString() throws Throwable {
        // Test conversion of invalid string to byte array
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.toByteArray("\"S|Pn_%?u{!|");
        assertArrayEquals(new byte[]{(byte) 0}, result);
    }

    @Test(timeout = 4000)
    public void testIsEmptyWithNullArray() throws Throwable {
        // Verify isEmpty returns true for null input
        assertTrue(BinaryCodec.isEmpty((byte[]) null));
    }

    @Test(timeout = 4000)
    public void testIsEmptyWithNonEmptyArray() throws Throwable {
        // Verify isEmpty returns false for non-empty array
        byte[] input = new byte[8];
        assertFalse(BinaryCodec.isEmpty(input));
    }

    @Test(timeout = 4000)
    public void testEncodeNullByteArray() throws Throwable {
        // Test encoding null returns empty array
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.encode((byte[]) null);
        assertArrayEquals(new byte[]{}, result);
    }

    @Test(timeout = 4000)
    public void testDecodeByteArray() throws Throwable {
        // Test decoding a byte array
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[8];
        byte[] result = codec.decode(input);
        assertArrayEquals(new byte[]{(byte) 0}, result);
    }

    @Test(timeout = 4000)
    public void testToAsciiStringThrowsAfterMultipleNestings() throws Throwable {
        // Verify excessive nesting causes exception in toAsciiString
        byte[] original = new byte[5];
        byte[] nested1 = BinaryCodec.toAsciiBytes(original);
        byte[] nested2 = BinaryCodec.toAsciiBytes(nested1);
        byte[] nested3 = BinaryCodec.toAsciiBytes(nested2);

        try {
            BinaryCodec.toAsciiString(nested3);
            fail("Expected exception due to excessive nesting");
        } catch (Throwable e) {
            verifyException("org.apache.commons.codec.binary.BinaryCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testToAsciiCharsThrowsAfterMultipleNestings() throws Throwable {
        // Verify excessive nesting causes exception in toAsciiChars
        byte[] original = new byte[5];
        byte[] nested1 = BinaryCodec.toAsciiBytes(original);
        byte[] nested2 = BinaryCodec.toAsciiBytes(nested1);
        byte[] nested3 = BinaryCodec.toAsciiBytes(nested2);

        try {
            BinaryCodec.toAsciiChars(nested3);
            fail("Expected exception due to excessive nesting");
        } catch (Throwable e) {
            verifyException("org.apache.commons.codec.binary.BinaryCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testToAsciiBytesThrowsAfterMultipleNestings() throws Throwable {
        // Verify excessive nesting causes exception in toAsciiBytes
        byte[] original = new byte[16];
        byte[] nested1 = BinaryCodec.toAsciiBytes(original);
        byte[] nested2 = BinaryCodec.toAsciiBytes(nested1);

        try {
            BinaryCodec.toAsciiBytes(nested2);
            fail("Expected exception due to excessive nesting");
        } catch (Throwable e) {
            verifyException("org.apache.commons.codec.binary.BinaryCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeThrowsAfterMultipleNestings() throws Throwable {
        // Verify excessive nesting causes exception in encode()
        BinaryCodec codec = new BinaryCodec();
        byte[] original = new byte[17];
        byte[] nested1 = BinaryCodec.toAsciiBytes(original);
        byte[] nested2 = BinaryCodec.toAsciiBytes(nested1);

        try {
            codec.encode(nested2);
            fail("Expected exception due to excessive nesting");
        } catch (Throwable e) {
            verifyException("org.apache.commons.codec.binary.BinaryCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testToAsciiCharsWithNullInput() throws Throwable {
        // Test toAsciiChars with null returns empty array
        char[] result = BinaryCodec.toAsciiChars((byte[]) null);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void testFromAsciiWithEmptyCharArray() throws Throwable {
        // Test fromAscii with empty char array
        char[] input = new char[0];
        byte[] result = BinaryCodec.fromAscii(input);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void testToByteArrayWithNullString() throws Throwable {
        // Test toByteArray with null string
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.toByteArray((String) null);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void testEncodeNonByteArrayObjectThrowsException() throws Throwable {
        // Verify encode() throws when passed non-byte-array object
        BinaryCodec codec = new BinaryCodec();
        try {
            codec.encode((Object) codec);
            fail("Expected exception for invalid argument type");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.binary.BinaryCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeNonByteArrayObjectThrowsException() throws Throwable {
        // Verify decode() throws when passed non-byte-array object
        BinaryCodec codec = new BinaryCodec();
        try {
            codec.decode((Object) codec);
            fail("Expected exception for invalid argument type");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.binary.BinaryCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeStringObject() throws Throwable {
        // Test decoding a string object
        BinaryCodec codec = new BinaryCodec();
        Object decoded = codec.decode((Object) "(mj0N>],r1/P");
        Object redecoded = codec.decode(decoded);
        assertNotSame(redecoded, decoded);
    }

    @Test(timeout = 4000)
    public void testToAsciiBytesWithNullInput() throws Throwable {
        // Test toAsciiBytes with null returns empty array
        byte[] result = BinaryCodec.toAsciiBytes((byte[]) null);
        assertArrayEquals(new byte[]{}, result);
    }

    @Test(timeout = 4000)
    public void testAsciiRoundTripWithNonZeroByte() throws Throwable {
        // Test round-trip conversion (toAsciiChars -> fromAscii)
        byte[] original = new byte[1];
        original[0] = (byte) 64; // '@' character

        char[] asciiChars = BinaryCodec.toAsciiChars(original);
        byte[] result = BinaryCodec.fromAscii(asciiChars);

        assertArrayEquals(new char[]{'0', '1', '0', '0', '0', '0', '0', '0'}, asciiChars);
        assertArrayEquals(new byte[]{(byte) 64}, result);
    }

    @Test(timeout = 4000)
    public void testDecodeEncodedNull() throws Throwable {
        // Test decoding an encoded null object
        BinaryCodec codec = new BinaryCodec();
        Object decodedNull = codec.decode((Object) null);
        Object encodedNull = codec.encode(decodedNull);
        Object redecoded = codec.decode(encodedNull);
        assertSame(redecoded, decodedNull);
    }

    @Test(timeout = 4000)
    public void testToByteArrayAndToAsciiString() throws Throwable {
        // Test conversion from string to byte array to ASCII string
        BinaryCodec codec = new BinaryCodec();
        byte[] bytes = codec.toByteArray("lb{1yPz");
        String asciiString = BinaryCodec.toAsciiString(bytes);
        assertEquals("", asciiString);
    }

    @Test(timeout = 4000)
    public void testFromAsciiWithNullCharArray() throws Throwable {
        // Test fromAscii with null char array
        byte[] result = BinaryCodec.fromAscii((char[]) null);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void testFromAsciiWithByteArrayContaining48() throws Throwable {
        // Test fromAscii with byte array containing '0' (ASCII 48)
        byte[] input = new byte[3];
        input[2] = (byte) 48; // '0' character
        byte[] asciiBytes = BinaryCodec.toAsciiBytes(input);
        byte[] result = BinaryCodec.fromAscii(asciiBytes);
        assertArrayEquals(new byte[]{(byte) 0, (byte) 0, (byte) 48}, result);
    }

    @Test(timeout = 4000)
    public void testFromAsciiWithEmptyByteArray() throws Throwable {
        // Test fromAscii with empty byte array
        byte[] input = new byte[0];
        byte[] result = BinaryCodec.fromAscii(input);
        assertNotSame(result, input);
    }

    @Test(timeout = 4000)
    public void testToAsciiStringWithSingleZeroByte() throws Throwable {
        // Test toAsciiString with single zero byte
        byte[] input = new byte[1]; // Defaults to [0]
        String result = BinaryCodec.toAsciiString(input);
        assertEquals("00000000", result);
    }

    @Test(timeout = 4000)
    public void testEncodeByteArrayOf17Zeros() throws Throwable {
        // Test encoding 17 zero bytes
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[17];
        byte[] result = codec.encode(input);
        assertEquals(136, result.length); // 17 bytes * 8 bits/byte = 136
    }

    @Test(timeout = 4000)
    public void testDecodeWithSingleZeroByte() throws Throwable {
        // Test decoding a single zero byte
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[1]; // Single zero byte
        byte[] result = codec.decode(input);
        assertEquals(0, result.length);
    }
}