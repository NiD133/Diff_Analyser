package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.StringUtils;
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
public class StringUtils_ESTest extends StringUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testConvertCharsToBytesWithNonEmptyArray() throws Throwable {
        // Test converting a non-empty char array to a byte array
        char[] charArray = new char[8];
        charArray[0] = 'h';
        byte[] byteArray = StringUtils.convertCharsToBytes(charArray);
        assertEquals(16, byteArray.length);
    }

    @Test(timeout = 4000)
    public void testConvertCharsToBytesWithEmptyArray() throws Throwable {
        // Test converting an empty char array to a byte array
        char[] charArray = new char[0];
        byte[] byteArray = StringUtils.convertCharsToBytes(charArray);
        assertEquals(0, byteArray.length);
    }

    @Test(timeout = 4000)
    public void testEscapeStringWithNullByteBuffer() throws Throwable {
        // Test escaping a byte array with a null ByteBuffer
        byte[] byteArray = new byte[3];
        try {
            StringUtils.escapeString(byteArray, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.StringUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testEscapeStringWithNegativeByteBufferCount() throws Throwable {
        // Test escaping a byte array with a ByteBuffer having negative count
        byte[] byteArray = new byte[0];
        ByteBuffer byteBuffer = new ByteBuffer();
        byteBuffer.count = -27;
        try {
            StringUtils.escapeString(byteArray, byteBuffer);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.itextpdf.text.pdf.ByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testEscapeStringWithNullByteArray() throws Throwable {
        // Test escaping a null byte array
        try {
            StringUtils.escapeString((byte[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.StringUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testConvertCharsToBytesWithNullCharArray() throws Throwable {
        // Test converting a null char array to a byte array
        try {
            StringUtils.convertCharsToBytes((char[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.StringUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testEscapeStringWithNonEmptyByteArray() throws Throwable {
        // Test escaping a non-empty byte array
        byte[] byteArray = new byte[6];
        byteArray[4] = (byte) 12;
        ByteBuffer byteBuffer = new ByteBuffer((byte) (-35));
        StringUtils.escapeString(byteArray, byteBuffer);
        assertEquals(9, byteBuffer.size());
    }

    @Test(timeout = 4000)
    public void testEscapeStringWithByteArrayHavingSpecificByte() throws Throwable {
        // Test escaping a byte array with a specific byte value
        byte[] byteArray = new byte[8];
        byteArray[3] = (byte) 8;
        ByteBuffer byteBuffer = new ByteBuffer();
        StringUtils.escapeString(byteArray, byteBuffer);
        assertEquals(11, byteBuffer.size());
    }

    @Test(timeout = 4000)
    public void testEscapeStringWithAnotherSpecificByte() throws Throwable {
        // Test escaping a byte array with another specific byte value
        byte[] byteArray = new byte[5];
        byteArray[2] = (byte) 13;
        ByteBuffer byteBuffer = new ByteBuffer();
        StringUtils.escapeString(byteArray, byteBuffer);
        assertEquals(8, byteBuffer.size());
    }

    @Test(timeout = 4000)
    public void testEscapeStringWithMultipleByteArrays() throws Throwable {
        // Test escaping a byte array and then escaping the result again
        byte[] byteArray = new byte[4];
        byteArray[1] = (byte) 9;
        ByteBuffer byteBuffer = new ByteBuffer(71);
        byte[] escapedArray = StringUtils.escapeString(byteArray);
        StringUtils.escapeString(escapedArray, byteBuffer);
        assertEquals(12, byteBuffer.size());
        assertArrayEquals(new byte[] {(byte) 40, (byte) 0, (byte) 92, (byte) 116, (byte) 0, (byte) 0, (byte) 41}, escapedArray);
    }

    @Test(timeout = 4000)
    public void testEscapeStringWithByteArrayAndByteBuffer() throws Throwable {
        // Test escaping a byte array with a ByteBuffer
        ByteBuffer byteBuffer = new ByteBuffer((byte) 10);
        byte[] byteArray = new byte[7];
        byteArray[5] = (byte) 10;
        StringUtils.escapeString(byteArray, byteBuffer);
        assertEquals(10, byteBuffer.size());
    }

    @Test(timeout = 4000)
    public void testEscapeStringWithByteArrayAndSpecificByteBufferSize() throws Throwable {
        // Test escaping a byte array with a ByteBuffer of specific size
        byte[] byteArray = new byte[4];
        byteArray[1] = (byte) 9;
        ByteBuffer byteBuffer = new ByteBuffer(71);
        StringUtils.escapeString(byteArray, byteBuffer);
        assertEquals(7, byteBuffer.size());
    }
}