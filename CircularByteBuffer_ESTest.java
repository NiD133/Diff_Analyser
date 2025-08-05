package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.io.input.buffer.CircularByteBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the CircularByteBuffer class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CircularByteBuffer_ESTest extends CircularByteBuffer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testReadWithInvalidLengthThrowsIllegalArgumentException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[4];

        try {
            buffer.read(byteArray, 1, 104);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithInsufficientBytesThrowsIllegalStateException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[4];

        try {
            buffer.read(byteArray, 3, 1);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithZeroLength() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[1];
        buffer.read(byteArray, 0, 0);
        assertEquals(8192, buffer.getSpace());
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidOffsetThrowsIllegalArgumentException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[1];

        try {
            buffer.read(byteArray, 1, 1);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddAndReadSingleByte() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.add((byte) 0);
        buffer.add((byte) 0);
        byte result = buffer.read();
        assertTrue(buffer.hasBytes());
        assertEquals((byte) 0, result);
    }

    @Test(timeout = 4000)
    public void testPeekWithInsufficientSpace() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[7];
        byteArray[0] = (byte) 127;
        boolean result = buffer.peek(byteArray, 0, 25);
        assertFalse(result);
        assertEquals(8192, buffer.getSpace());
    }

    @Test(timeout = 4000)
    public void testPeekAfterRead() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[2];
        buffer.add((byte) 0);
        buffer.read();
        boolean result = buffer.peek(byteArray, 0, 1);
        assertFalse(buffer.hasBytes());
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testPeekWithNegativeLengthThrowsIllegalArgumentException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[0];

        try {
            buffer.peek(byteArray, 0, -3297);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testHasSpaceForSpecificSize() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        boolean result = buffer.hasSpace(1024);
        assertTrue(result);
        assertEquals(8192, buffer.getSpace());
    }

    @Test(timeout = 4000)
    public void testGetSpaceAfterAddingByte() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.add((byte) 0);
        int space = buffer.getSpace();
        assertEquals(1, buffer.getCurrentNumberOfBytes());
        assertEquals(8191, space);
    }

    @Test(timeout = 4000)
    public void testAddWithInvalidOffsetThrowsIllegalArgumentException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer(0);
        byte[] byteArray = new byte[3];

        try {
            buffer.add(byteArray, 1886, 0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddWithZeroLengthThrowsIllegalArgumentException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[0];

        try {
            buffer.add(byteArray, 0, 0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddAndReadNegativeByte() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.add((byte) -31);
        byte result = buffer.read();
        assertEquals(8192, buffer.getSpace());
        assertEquals((byte) -31, result);
    }

    @Test(timeout = 4000)
    public void testGetSpaceForZeroSizeBuffer() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer(0);
        int space = buffer.getSpace();
        assertEquals(0, space);
    }

    @Test(timeout = 4000)
    public void testGetCurrentNumberOfBytesAfterAddingByte() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.add((byte) 0);
        int numberOfBytes = buffer.getCurrentNumberOfBytes();
        assertEquals(8191, buffer.getSpace());
        assertEquals(1, numberOfBytes);
    }

    @Test(timeout = 4000)
    public void testReadWithNullBufferThrowsNullPointerException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();

        try {
            buffer.read(null, 30, 30);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testPeekWithNullBufferThrowsNullPointerException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();

        try {
            buffer.peek(null, 1254, 1254);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testPeekWithOutOfBoundsOffsetThrowsArrayIndexOutOfBoundsException() throws Throwable {
        byte[] byteArray = new byte[22];
        CircularByteBuffer buffer = new CircularByteBuffer();

        try {
            buffer.peek(byteArray, 16, 16);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddWithNullBufferThrowsNullPointerException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer(978);

        try {
            buffer.add(null, 978, 978);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddWithOutOfBoundsLengthThrowsArrayIndexOutOfBoundsException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[7];

        try {
            buffer.add(byteArray, 0, 9);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testNegativeBufferSizeThrowsNegativeArraySizeException() throws Throwable {
        try {
            CircularByteBuffer buffer = new CircularByteBuffer(-21);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            verifyException("org.apache.commons.io.IOUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidLengthThrowsIllegalArgumentException() throws Throwable {
        byte[] byteArray = new byte[2];
        CircularByteBuffer buffer = new CircularByteBuffer(0);

        try {
            buffer.read(byteArray, 0, 193);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeLengthThrowsIllegalArgumentException() throws Throwable {
        byte[] byteArray = new byte[16];
        CircularByteBuffer buffer = new CircularByteBuffer();

        try {
            buffer.read(byteArray, 2, -65);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidOffsetThrowsIllegalArgumentException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[1];

        try {
            buffer.read(byteArray, 7, 7);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithInsufficientBytesThrowsIllegalStateException() throws Throwable {
        byte[] byteArray = new byte[22];
        CircularByteBuffer buffer = new CircularByteBuffer(2);

        try {
            buffer.read(byteArray, 2, 2);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeOffsetThrowsIllegalArgumentException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[4];

        try {
            buffer.read(byteArray, -3129, 1);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadFromEmptyBufferThrowsIllegalStateException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();

        try {
            buffer.read();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testPeekWithValidLength() throws Throwable {
        byte[] byteArray = new byte[16];
        CircularByteBuffer buffer = new CircularByteBuffer(2);
        boolean result = buffer.peek(byteArray, 2, 2);
        assertEquals(2, buffer.getSpace());
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testPeekWithInsufficientBytes() throws Throwable {
        byte[] byteArray = new byte[16];
        CircularByteBuffer buffer = new CircularByteBuffer(2);
        buffer.add((byte) 105);
        boolean result = buffer.peek(byteArray, 2, 2);
        assertEquals(1, buffer.getSpace());
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testPeekWithZeroLength() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.add((byte) 47);
        byte[] byteArray = new byte[9];
        boolean result = buffer.peek(byteArray, 0, 0);
        assertEquals(1, buffer.getCurrentNumberOfBytes());
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testPeekWithInvalidLengthThrowsIllegalArgumentException() throws Throwable {
        byte[] byteArray = new byte[15];
        CircularByteBuffer buffer = new CircularByteBuffer(2);

        try {
            buffer.peek(byteArray, 2, 255);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testPeekWithNegativeLengthThrowsIllegalArgumentException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[7];

        try {
            buffer.peek(byteArray, 0, -2328);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testPeekWithInvalidOffsetThrowsIllegalArgumentException() throws Throwable {
        byte[] byteArray = new byte[4];
        CircularByteBuffer buffer = new CircularByteBuffer();

        try {
            buffer.peek(byteArray, 7, 7);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testPeekWithZeroLength() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[2];
        boolean result = buffer.peek(byteArray, 0, 0);
        assertEquals(8192, buffer.getSpace());
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testPeekWithNegativeOffsetThrowsIllegalArgumentException() throws Throwable {
        byte[] byteArray = new byte[1];
        CircularByteBuffer buffer = new CircularByteBuffer();

        try {
            buffer.peek(byteArray, -1, -1);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testHasSpaceForZeroSizeBuffer() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer(0);
        boolean result = buffer.hasSpace(0);
        assertTrue(result);
        assertFalse(buffer.hasSpace());
    }

    @Test(timeout = 4000)
    public void testHasSpaceForMaxValue() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        boolean result = buffer.hasSpace(Integer.MAX_VALUE);
        assertFalse(result);
        assertEquals(8192, buffer.getSpace());
    }

    @Test(timeout = 4000)
    public void testHasSpaceForDefaultBuffer() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        boolean result = buffer.hasSpace();
        assertEquals(8192, buffer.getSpace());
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testHasSpaceForZeroSizeBuffer() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer(0);
        boolean result = buffer.hasSpace();
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testHasBytesForEmptyBuffer() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer(2);
        boolean result = buffer.hasBytes();
        assertEquals(2, buffer.getSpace());
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testAddAndReadMultipleBytes() throws Throwable {
        byte[] byteArray = new byte[22];
        CircularByteBuffer buffer = new CircularByteBuffer(2);
        buffer.add(byteArray, 2, 2);
        assertFalse(buffer.hasSpace());

        buffer.read(byteArray, 2, 2);
        assertEquals(0, buffer.getCurrentNumberOfBytes());
    }

    @Test(timeout = 4000)
    public void testAddWithInsufficientSpaceThrowsIllegalStateException() throws Throwable {
        byte[] byteArray = new byte[22];
        CircularByteBuffer buffer = new CircularByteBuffer(2);

        try {
            buffer.add(byteArray, 2, 8192);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddWithNegativeLengthThrowsIllegalArgumentException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[9];

        try {
            buffer.add(byteArray, 0, -2101);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddWithZeroLength() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] byteArray = new byte[8];
        buffer.add(byteArray, 0, 0);
        assertEquals(8192, buffer.getSpace());
    }

    @Test(timeout = 4000)
    public void testAddWithNegativeOffsetThrowsIllegalArgumentException() throws Throwable {
        byte[] byteArray = new byte[16];
        CircularByteBuffer buffer = new CircularByteBuffer();

        try {
            buffer.add(byteArray, -4, -4);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddAndReadSingleByteWithFullBuffer() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer(1);
        buffer.add((byte) 1);
        assertEquals(0, buffer.getSpace());

        byte result = buffer.read();
        assertEquals(1, buffer.getSpace());
        assertEquals((byte) 1, result);
    }

    @Test(timeout = 4000)
    public void testAddToFullBufferThrowsIllegalStateException() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer(0);

        try {
            buffer.add((byte) 0);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.io.input.buffer.CircularByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testHasBytesAfterAddingByte() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.add((byte) 0);
        boolean result = buffer.hasBytes();
        assertEquals(1, buffer.getCurrentNumberOfBytes());
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testGetCurrentNumberOfBytesForZeroSizeBuffer() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer(0);
        int numberOfBytes = buffer.getCurrentNumberOfBytes();
        assertFalse(buffer.hasSpace());
        assertEquals(0, numberOfBytes);
    }

    @Test(timeout = 4000)
    public void testClearBuffer() throws Throwable {
        CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.clear();
        assertEquals(8192, buffer.getSpace());
    }
}