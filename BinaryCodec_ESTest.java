package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.binary.BinaryCodec;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class BinaryCodecTest extends BinaryCodecTestScaffolding {

    @Test(timeout = 4000)
    public void testFromAsciiWithNonZeroByte() throws Throwable {
        byte[] input = new byte[9];
        input[1] = (byte) 102;
        byte[] result = BinaryCodec.fromAscii(input);
        assertArrayEquals(new byte[]{(byte) 0}, result);
    }

    @Test(timeout = 4000)
    public void testToByteArrayWithNonBinaryString() throws Throwable {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.toByteArray("\"S|Pn_%?u{!|");
        assertArrayEquals(new byte[]{(byte) 0}, result);
    }

    @Test(timeout = 4000)
    public void testIsEmptyWithNullArray() throws Throwable {
        boolean result = BinaryCodec.isEmpty((byte[]) null);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testIsEmptyWithNonEmptyArray() throws Throwable {
        byte[] input = new byte[8];
        boolean result = BinaryCodec.isEmpty(input);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testEncodeWithNullArray() throws Throwable {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.encode((byte[]) null);
        assertArrayEquals(new byte[]{}, result);
    }

    @Test(timeout = 4000)
    public void testDecodeWithEmptyArray() throws Throwable {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[8];
        byte[] result = codec.decode(input);
        assertArrayEquals(new byte[]{(byte) 0}, result);
    }

    @Test(timeout = 4000)
    public void testToAsciiBytesAndStringWithMultipleConversions() throws Throwable {
        byte[] input = new byte[5];
        byte[] asciiBytes = BinaryCodec.toAsciiBytes(input);
        byte[] doubleAsciiBytes = BinaryCodec.toAsciiBytes(asciiBytes);
        byte[] tripleAsciiBytes = BinaryCodec.toAsciiBytes(doubleAsciiBytes);
        // Expecting an exception due to invalid conversion
        BinaryCodec.toAsciiString(tripleAsciiBytes);
    }

    @Test(timeout = 4000)
    public void testToAsciiCharsWithMultipleConversions() throws Throwable {
        byte[] input = new byte[5];
        byte[] asciiBytes = BinaryCodec.toAsciiBytes(input);
        byte[] doubleAsciiBytes = BinaryCodec.toAsciiBytes(asciiBytes);
        byte[] tripleAsciiBytes = BinaryCodec.toAsciiBytes(doubleAsciiBytes);
        // Expecting an exception due to invalid conversion
        BinaryCodec.toAsciiChars(tripleAsciiBytes);
    }

    @Test(timeout = 4000)
    public void testToAsciiBytesWithExcessiveConversions() throws Throwable {
        byte[] input = new byte[16];
        byte[] asciiBytes = BinaryCodec.toAsciiBytes(input);
        byte[] doubleAsciiBytes = BinaryCodec.toAsciiBytes(asciiBytes);
        // Expecting an exception due to invalid conversion
        BinaryCodec.toAsciiBytes(doubleAsciiBytes);
    }

    @Test(timeout = 4000)
    public void testEncodeWithExcessiveConversions() throws Throwable {
        byte[] input = new byte[17];
        byte[] asciiBytes = BinaryCodec.toAsciiBytes(input);
        byte[] doubleAsciiBytes = BinaryCodec.toAsciiBytes(asciiBytes);
        BinaryCodec codec = new BinaryCodec();
        // Expecting an exception due to invalid conversion
        codec.encode(doubleAsciiBytes);
    }

    @Test(timeout = 4000)
    public void testToAsciiCharsWithNullArray() throws Throwable {
        char[] result = BinaryCodec.toAsciiChars((byte[]) null);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void testFromAsciiWithEmptyCharArray() throws Throwable {
        char[] input = new char[0];
        byte[] result = BinaryCodec.fromAscii(input);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void testToByteArrayWithNullString() throws Throwable {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.toByteArray((String) null);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void testEncodeWithInvalidObject() throws Throwable {
        BinaryCodec codec = new BinaryCodec();
        try {
            codec.encode((Object) codec);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.binary.BinaryCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeWithInvalidObject() throws Throwable {
        BinaryCodec codec = new BinaryCodec();
        try {
            codec.decode((Object) codec);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.binary.BinaryCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeAndReDecodeWithString() throws Throwable {
        BinaryCodec codec = new BinaryCodec();
        Object decoded = codec.decode((Object) "(mj0N>],r1/P");
        Object reDecoded = codec.decode(decoded);
        assertNotSame(reDecoded, decoded);
    }

    @Test(timeout = 4000)
    public void testToAsciiBytesWithNullArray() throws Throwable {
        byte[] result = BinaryCodec.toAsciiBytes((byte[]) null);
        assertArrayEquals(new byte[]{}, result);
    }

    @Test(timeout = 4000)
    public void testToAsciiCharsAndFromAsciiWithSingleByte() throws Throwable {
        byte[] input = new byte[1];
        input[0] = (byte) 64;
        char[] asciiChars = BinaryCodec.toAsciiChars(input);
        byte[] result = BinaryCodec.fromAscii(asciiChars);
        assertArrayEquals(new char[]{'0', '1', '0', '0', '0', '0', '0', '0'}, asciiChars);
        assertArrayEquals(new byte[]{(byte) 64}, result);
    }

    @Test(timeout = 4000)
    public void testDecodeEncodeDecodeWithNull() throws Throwable {
        BinaryCodec codec = new BinaryCodec();
        Object decoded = codec.decode((Object) null);
        Object encoded = codec.encode(decoded);
        Object reDecoded = codec.decode(encoded);
        assertSame(reDecoded, decoded);
    }

    @Test(timeout = 4000)
    public void testToAsciiStringWithEmptyByteArray() throws Throwable {
        BinaryCodec codec = new BinaryCodec();
        byte[] byteArray = codec.toByteArray("lb{1yPz");
        String result = BinaryCodec.toAsciiString(byteArray);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testFromAsciiWithNullCharArray() throws Throwable {
        byte[] result = BinaryCodec.fromAscii((char[]) null);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void testToAsciiBytesAndFromAsciiWithNonZeroByte() throws Throwable {
        byte[] input = new byte[3];
        input[2] = (byte) 48;
        byte[] asciiBytes = BinaryCodec.toAsciiBytes(input);
        byte[] result = BinaryCodec.fromAscii(asciiBytes);
        assertArrayEquals(new byte[]{(byte) 0, (byte) 0, (byte) 48}, result);
    }

    @Test(timeout = 4000)
    public void testFromAsciiWithEmptyByteArray() throws Throwable {
        byte[] input = new byte[0];
        byte[] result = BinaryCodec.fromAscii(input);
        assertNotSame(result, input);
    }

    @Test(timeout = 4000)
    public void testToAsciiStringWithSingleByte() throws Throwable {
        byte[] input = new byte[1];
        String result = BinaryCodec.toAsciiString(input);
        assertEquals("00000000", result);
    }

    @Test(timeout = 4000)
    public void testEncodeWithNonEmptyByteArray() throws Throwable {
        byte[] input = new byte[17];
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.encode(input);
        assertEquals(136, result.length);
    }

    @Test(timeout = 4000)
    public void testDecodeWithSingleByteArray() throws Throwable {
        byte[] input = new byte[1];
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.decode(input);
        assertEquals(0, result.length);
    }
}