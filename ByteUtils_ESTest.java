package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.io.*;

import org.apache.commons.compress.utils.ByteUtils;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.junit.runner.RunWith;

/**
 * Test suite for ByteUtils class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ByteUtils_ESTest extends ByteUtils_ESTest_scaffolding {

    /**
     * Test writing to a ByteArrayOutputStream using toLittleEndian.
     */
    @Test(timeout = 4000)
    public void testWriteToByteArrayOutputStream() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteUtils.toLittleEndian(byteArrayOutputStream, -392L, 1633);
        assertEquals(1633, byteArrayOutputStream.size());
    }

    /**
     * Test writing to a MockFileOutputStream using toLittleEndian.
     */
    @Test(timeout = 4000)
    public void testWriteToMockFileOutputStream() throws Throwable {
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("%`QcQ", true);
        ByteUtils.toLittleEndian(mockFileOutputStream, 8L, -1);
    }

    /**
     * Test writing to a DataOutputStream using toLittleEndian with negative length.
     */
    @Test(timeout = 4000)
    public void testWriteToDataOutputStreamWithNegativeLength() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        ByteUtils.toLittleEndian(dataOutputStream, -1257L, -2038);
        assertEquals(0, byteArrayOutputStream.size());
    }

    /**
     * Test writing to a null ByteConsumer using toLittleEndian.
     */
    @Test(timeout = 4000)
    public void testWriteToNullByteConsumer() throws Throwable {
        ByteUtils.toLittleEndian((ByteUtils.ByteConsumer) null, 0L, -2448);
    }

    /**
     * Test writing to a byte array using toLittleEndian.
     */
    @Test(timeout = 4000)
    public void testWriteToByteArray() throws Throwable {
        byte[] byteArray = new byte[2];
        ByteUtils.toLittleEndian(byteArray, -30L, 1, 1);
        assertArrayEquals(new byte[] {(byte) 0, (byte) -30}, byteArray);
    }

    /**
     * Test writing to a byte array with negative length using toLittleEndian.
     */
    @Test(timeout = 4000)
    public void testWriteToByteArrayWithNegativeLength() throws Throwable {
        byte[] byteArray = new byte[2];
        ByteUtils.toLittleEndian(byteArray, -10L, 8, -2448);
        assertEquals(2, byteArray.length);
    }

    /**
     * Test reading from a ByteArrayInputStream using fromLittleEndian.
     */
    @Test(timeout = 4000)
    public void testReadFromByteArrayInputStream() throws Throwable {
        byte[] byteArray = new byte[7];
        byteArray[2] = (byte) 91;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        long result = ByteUtils.fromLittleEndian(byteArrayInputStream, 4);
        assertEquals(3, byteArrayInputStream.available());
        assertEquals(5963776L, result);
    }

    /**
     * Test reading from a DataInputStream using fromLittleEndian.
     */
    @Test(timeout = 4000)
    public void testReadFromDataInputStream() throws Throwable {
        byte[] byteArray = new byte[5];
        byteArray[1] = (byte) -32;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        long result = ByteUtils.fromLittleEndian(dataInputStream, 5);
        assertEquals(0, byteArrayInputStream.available());
        assertEquals(57344L, result);
    }

    /**
     * Test reading from a complex InputStream setup using fromLittleEndian.
     */
    @Test(timeout = 4000)
    public void testReadFromComplexInputStream() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(pipedInputStream);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(pushbackInputStream);
        DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
        long result = ByteUtils.fromLittleEndian(dataInputStream, -325);
        assertEquals(0L, result);
    }

    /**
     * Test reading from a mocked ByteSupplier using fromLittleEndian.
     */
    @Test(timeout = 4000)
    public void testReadFromMockedByteSupplier() throws Throwable {
        ByteUtils.ByteSupplier byteSupplier = mock(ByteUtils.ByteSupplier.class, new ViolatedAssumptionAnswer());
        doReturn(-325, -250, 1623, 0, -325).when(byteSupplier).getAsByte();
        long result = ByteUtils.fromLittleEndian(byteSupplier, 8);
        assertEquals(-325L, result);
    }

    /**
     * Test reading from a byte array with invalid length using fromLittleEndian.
     */
    @Test(timeout = 4000)
    public void testReadFromByteArrayWithInvalidLength() throws Throwable {
        byte[] byteArray = new byte[2];
        long result = ByteUtils.fromLittleEndian(byteArray, 3737, -10);
        assertEquals(0L, result);
    }

    /**
     * Test using OutputStreamByteConsumer to write a single byte.
     */
    @Test(timeout = 4000)
    public void testOutputStreamByteConsumer() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteUtils.OutputStreamByteConsumer byteConsumer = new ByteUtils.OutputStreamByteConsumer(byteArrayOutputStream);
        byteConsumer.accept(1);
        assertEquals("\u0001", byteArrayOutputStream.toString());
    }

    /**
     * Test handling of ArrayIndexOutOfBoundsException in toLittleEndian.
     */
    @Test(timeout = 4000)
    public void testArrayIndexOutOfBoundsExceptionInToLittleEndian() throws Throwable {
        byte[] byteArray = new byte[3];
        try {
            ByteUtils.toLittleEndian(byteArray, 1L, 1, 63);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test reading from a byte array using fromLittleEndian.
     */
    @Test(timeout = 4000)
    public void testReadFromByteArray() throws Throwable {
        byte[] byteArray = new byte[8];
        byteArray[1] = (byte) -68;
        long result = ByteUtils.fromLittleEndian(byteArray, 1, 1);
        assertEquals(188L, result);
    }

    /**
     * Test reading from a byte array using fromLittleEndian.
     */
    @Test(timeout = 4000)
    public void testReadFromByteArrayFull() throws Throwable {
        byte[] byteArray = new byte[8];
        byteArray[5] = (byte) -5;
        long result = ByteUtils.fromLittleEndian(byteArray);
        assertEquals(275977418571776L, result);
    }

    /**
     * Test reading from a byte array with negative value using fromLittleEndian.
     */
    @Test(timeout = 4000)
    public void testReadFromByteArrayWithNegativeValue() throws Throwable {
        byte[] byteArray = new byte[8];
        byteArray[7] = (byte) -72;
        long result = ByteUtils.fromLittleEndian(byteArray);
        assertEquals(-5188146770730811392L, result);
    }

    /**
     * Test reading from a mocked ByteSupplier using fromLittleEndian.
     */
    @Test(timeout = 4000)
    public void testReadFromMockedByteSupplierSingleByte() throws Throwable {
        ByteUtils.ByteSupplier byteSupplier = mock(ByteUtils.ByteSupplier.class, new ViolatedAssumptionAnswer());
        doReturn(8).when(byteSupplier).getAsByte();
        long result = ByteUtils.fromLittleEndian(byteSupplier, 1);
        assertEquals(8L, result);
    }

    /**
     * Test handling of NullPointerException in toLittleEndian with null byte array.
     */
    @Test(timeout = 4000)
    public void testNullPointerExceptionInToLittleEndianWithNullByteArray() throws Throwable {
        try {
            ByteUtils.toLittleEndian((byte[]) null, -2448L, 8, 8);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test handling of NullPointerException in toLittleEndian with null ByteConsumer.
     */
    @Test(timeout = 4000)
    public void testNullPointerExceptionInToLittleEndianWithNullByteConsumer() throws Throwable {
        try {
            ByteUtils.toLittleEndian((ByteUtils.ByteConsumer) null, -1659L, 63);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test handling of NullPointerException in toLittleEndian with null OutputStream.
     */
    @Test(timeout = 4000)
    public void testNullPointerExceptionInToLittleEndianWithNullOutputStream() throws Throwable {
        try {
            ByteUtils.toLittleEndian((OutputStream) null, -1L, 3);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test handling of IOException in toLittleEndian with unconnected PipedOutputStream.
     */
    @Test(timeout = 4000)
    public void testIOExceptionInToLittleEndianWithUnconnectedPipedOutputStream() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        try {
            ByteUtils.toLittleEndian(pipedOutputStream, 1L, 2970);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedOutputStream", e);
        }
    }

    /**
     * Test handling of NullPointerException in toLittleEndian with null DataOutput.
     */
    @Test(timeout = 4000)
    public void testNullPointerExceptionInToLittleEndianWithNullDataOutput() throws Throwable {
        try {
            ByteUtils.toLittleEndian((DataOutput) null, 0L, 872);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test handling of NullPointerException in fromLittleEndian with null byte array.
     */
    @Test(timeout = 4000)
    public void testNullPointerExceptionInFromLittleEndianWithNullByteArray() throws Throwable {
        try {
            ByteUtils.fromLittleEndian((byte[]) null, 8, 8);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test handling of NullPointerException in fromLittleEndian with null byte array.
     */
    @Test(timeout = 4000)
    public void testNullPointerExceptionInFromLittleEndianWithNullByteArrayFull() throws Throwable {
        try {
            ByteUtils.fromLittleEndian((byte[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test handling of IllegalArgumentException in fromLittleEndian with too large byte array.
     */
    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionInFromLittleEndianWithTooLargeByteArray() throws Throwable {
        byte[] byteArray = new byte[18];
        try {
            ByteUtils.fromLittleEndian(byteArray);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test handling of NullPointerException in fromLittleEndian with null ByteSupplier.
     */
    @Test(timeout = 4000)
    public void testNullPointerExceptionInFromLittleEndianWithNullByteSupplier() throws Throwable {
        try {
            ByteUtils.fromLittleEndian((ByteUtils.ByteSupplier) null, 1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test handling of IllegalArgumentException in fromLittleEndian with too large length.
     */
    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionInFromLittleEndianWithTooLargeLength() throws Throwable {
        try {
            ByteUtils.fromLittleEndian((ByteUtils.ByteSupplier) null, 1781);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test handling of NullPointerException in fromLittleEndian with null InputStream.
     */
    @Test(timeout = 4000)
    public void testNullPointerExceptionInFromLittleEndianWithNullInputStream() throws Throwable {
        try {
            ByteUtils.fromLittleEndian((InputStream) null, 8);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test handling of IllegalArgumentException in fromLittleEndian with too large length.
     */
    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionInFromLittleEndianWithTooLargeLengthForInputStream() throws Throwable {
        try {
            ByteUtils.fromLittleEndian((InputStream) null, 357);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test handling of ArrayIndexOutOfBoundsException in fromLittleEndian with invalid offset.
     */
    @Test(timeout = 4000)
    public void testArrayIndexOutOfBoundsExceptionInFromLittleEndianWithInvalidOffset() throws Throwable {
        byte[] byteArray = new byte[1];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray, -1, 6);
        try {
            ByteUtils.fromLittleEndian(byteArrayInputStream, 1);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // No message in exception
        }
    }

    /**
     * Test handling of NullPointerException in fromLittleEndian with null DataInput.
     */
    @Test(timeout = 4000)
    public void testNullPointerExceptionInFromLittleEndianWithNullDataInput() throws Throwable {
        try {
            ByteUtils.fromLittleEndian((DataInput) null, 1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test handling of IllegalArgumentException in fromLittleEndian with too large length.
     */
    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionInFromLittleEndianWithTooLargeLengthForDataInput() throws Throwable {
        try {
            ByteUtils.fromLittleEndian((DataInput) null, 1302);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test handling of IOException in fromLittleEndian with unconnected PipedInputStream.
     */
    @Test(timeout = 4000)
    public void testIOExceptionInFromLittleEndianWithUnconnectedPipedInputStream() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        DataInputStream dataInputStream = new DataInputStream(pipedInputStream);
        try {
            ByteUtils.fromLittleEndian(dataInputStream, 1);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedInputStream", e);
        }
    }

    /**
     * Test handling of EOFException in fromLittleEndian with insufficient data.
     */
    @Test(timeout = 4000)
    public void testEOFExceptionInFromLittleEndianWithInsufficientData() throws Throwable {
        byte[] byteArray = new byte[2];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        try {
            ByteUtils.fromLittleEndian(dataInputStream, 6);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("java.io.DataInputStream", e);
        }
    }

    /**
     * Test handling of ArrayIndexOutOfBoundsException in fromLittleEndian with invalid length.
     */
    @Test(timeout = 4000)
    public void testArrayIndexOutOfBoundsExceptionInFromLittleEndianWithInvalidLength() throws Throwable {
        byte[] byteArray = new byte[10];
        try {
            ByteUtils.fromLittleEndian(byteArray, 8, 8);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test writing to a DataOutputStream using toLittleEndian.
     */
    @Test(timeout = 4000)
    public void testWriteToDataOutputStream() throws Throwable {
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("zM{+~O5n/O/LjZTOA", true);
        DataOutputStream dataOutputStream = new DataOutputStream(mockFileOutputStream);
        ByteUtils.toLittleEndian(dataOutputStream, -294L, 2026);
    }

    /**
     * Test handling of IOException in fromLittleEndian with insufficient data in InputStream.
     */
    @Test(timeout = 4000)
    public void testIOExceptionInFromLittleEndianWithInsufficientDataInInputStream() throws Throwable {
        byte[] byteArray = new byte[6];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        try {
            ByteUtils.fromLittleEndian(byteArrayInputStream, 8);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test reading from a ByteArrayInputStream using fromLittleEndian.
     */
    @Test(timeout = 4000)
    public void testReadFromByteArrayInputStreamWithExactData() throws Throwable {
        byte[] byteArray = new byte[7];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        long result = ByteUtils.fromLittleEndian(byteArrayInputStream, 4);
        assertEquals(3, byteArrayInputStream.available());
        assertEquals(0L, result);
    }

    /**
     * Test reading from a DataInputStream using fromLittleEndian with negative length.
     */
    @Test(timeout = 4000)
    public void testReadFromDataInputStreamWithNegativeLength() throws Throwable {
        byte[] byteArray = new byte[5];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        long result = ByteUtils.fromLittleEndian(dataInputStream, -1);
        assertEquals(0L, result);
        assertEquals(5, byteArrayInputStream.available());
    }

    /**
     * Test handling of IOException in fromLittleEndian with mocked ByteSupplier.
     */
    @Test(timeout = 4000)
    public void testIOExceptionInFromLittleEndianWithMockedByteSupplier() throws Throwable {
        ByteUtils.ByteSupplier byteSupplier = mock(ByteUtils.ByteSupplier.class, new ViolatedAssumptionAnswer());
        doReturn(-1).when(byteSupplier).getAsByte();
        try {
            ByteUtils.fromLittleEndian(byteSupplier, 1);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test reading from a mocked ByteSupplier using fromLittleEndian.
     */
    @Test(timeout = 4000)
    public void testReadFromMockedByteSupplierWithZero() throws Throwable {
        ByteUtils.ByteSupplier byteSupplier = mock(ByteUtils.ByteSupplier.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(byteSupplier).getAsByte();
        long result = ByteUtils.fromLittleEndian(byteSupplier, 1);
        assertEquals(0L, result);
    }

    /**
     * Test reading from a null ByteSupplier using fromLittleEndian with negative length.
     */
    @Test(timeout = 4000)
    public void testReadFromNullByteSupplierWithNegativeLength() throws Throwable {
        long result = ByteUtils.fromLittleEndian((ByteUtils.ByteSupplier) null, -2049);
        assertEquals(0L, result);
    }

    /**
     * Test handling of IllegalArgumentException in fromLittleEndian with too large length.
     */
    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionInFromLittleEndianWithTooLargeLengthForByteArray() throws Throwable {
        byte[] byteArray = new byte[1];
        try {
            ByteUtils.fromLittleEndian(byteArray, 582, 582);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    /**
     * Test using OutputStreamByteConsumer to write multiple bytes.
     */
    @Test(timeout = 4000)
    public void testOutputStreamByteConsumerWithMultipleBytes() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        ByteUtils.OutputStreamByteConsumer byteConsumer = new ByteUtils.OutputStreamByteConsumer(dataOutputStream);
        ByteUtils.toLittleEndian(byteConsumer, -2038L, 20);
        assertEquals(20, byteArrayOutputStream.size());
        assertEquals("\n\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD\uFFFD", byteArrayOutputStream.toString());
    }

    /**
     * Test reading from a byte array using fromLittleEndian.
     */
    @Test(timeout = 4000)
    public void testReadFromByteArrayFullWithZero() throws Throwable {
        byte[] byteArray = new byte[2];
        long result = ByteUtils.fromLittleEndian(byteArray);
        assertEquals(0L, result);
    }
}