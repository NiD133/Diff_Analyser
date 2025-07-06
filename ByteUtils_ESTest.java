package org.apache.commons.compress.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.net.MockURI;

import java.io.*;
import java.net.URI;

import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ByteUtils_ESTest extends ByteUtils_ESTest_scaffolding {

    // Test for writing a long value to a BufferedOutputStream in little-endian format
    @Test(timeout = 4000)
    public void testWriteLongToBufferedOutputStream() throws Throwable {
        URI fileUri = MockURI.aFileURI;
        MockFile mockFile = new MockFile(fileUri);
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream(mockFile, false);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(mockFileOutputStream);
        ByteUtils.toLittleEndian(bufferedOutputStream, -1L, 502);
    }

    // Test for writing a long value to a PipedOutputStream in little-endian format
    @Test(timeout = 4000)
    public void testWriteLongToPipedOutputStream() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        ByteUtils.toLittleEndian(pipedOutputStream, 0L, -1);
    }

    // Test for writing a long value to a ByteArrayOutputStream using ByteConsumer
    @Test(timeout = 4000)
    public void testWriteLongToByteArrayOutputStream() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2449);
        ByteUtils.OutputStreamByteConsumer byteConsumer = new ByteUtils.OutputStreamByteConsumer(byteArrayOutputStream);
        ByteUtils.toLittleEndian(byteConsumer, -784L, 1215);
        assertEquals(1215, byteArrayOutputStream.size());
    }

    // Test for handling null ByteConsumer
    @Test(timeout = 4000)
    public void testNullByteConsumer() throws Throwable {
        ByteUtils.toLittleEndian((ByteUtils.ByteConsumer) null, 8L, -623);
    }

    // Test for writing a long value to a byte array in little-endian format
    @Test(timeout = 4000)
    public void testWriteLongToByteArray() throws Throwable {
        byte[] byteArray = new byte[0];
        ByteUtils.toLittleEndian(byteArray, 0L, 8, 0);
        assertEquals(0, byteArray.length);
    }

    // Test for reading a long value from a ByteArrayInputStream in little-endian format
    @Test(timeout = 4000)
    public void testReadLongFromByteArrayInputStream() throws Throwable {
        byte[] byteArray = new byte[7];
        byteArray[1] = (byte) 39;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        try {
            ByteUtils.fromLittleEndian(byteArrayInputStream, 8);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    // Test for reading a long value from a DataInputStream in little-endian format
    @Test(timeout = 4000)
    public void testReadLongFromDataInputStream() throws Throwable {
        byte[] byteArray = new byte[5];
        byteArray[1] = (byte) 8;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        try {
            ByteUtils.fromLittleEndian(dataInputStream, 8);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("java.io.DataInputStream", e);
        }
    }

    // Test for reading a long value from a DataInputStream with available bytes
    @Test(timeout = 4000)
    public void testReadLongFromDataInputStreamWithAvailableBytes() throws Throwable {
        byte[] byteArray = new byte[2];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        long result = ByteUtils.fromLittleEndian(dataInputStream, 2);
        assertEquals(0, byteArrayInputStream.available());
        assertEquals(0L, result);
    }

    // Test for handling null DataInput
    @Test(timeout = 4000)
    public void testNullDataInput() throws Throwable {
        long result = ByteUtils.fromLittleEndian((DataInput) null, -29);
        assertEquals(0L, result);
    }

    // Test for handling null ByteSupplier
    @Test(timeout = 4000)
    public void testNullByteSupplier() throws Throwable {
        long result = ByteUtils.fromLittleEndian((ByteUtils.ByteSupplier) null, -1);
        assertEquals(0L, result);
    }

    // Test for reading a long value from a byte array with negative offset and length
    @Test(timeout = 4000)
    public void testReadLongFromByteArrayWithNegativeOffsetAndLength() throws Throwable {
        byte[] byteArray = new byte[8];
        long result = ByteUtils.fromLittleEndian(byteArray, -181, -1);
        assertEquals(0L, result);
    }

    // Test for handling ArrayIndexOutOfBoundsException
    @Test(timeout = 4000)
    public void testArrayIndexOutOfBoundsException() throws Throwable {
        byte[] byteArray = new byte[8];
        try {
            ByteUtils.toLittleEndian(byteArray, -22L, 0, 51);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    // Test for reading a long value from a byte array with specific offset and length
    @Test(timeout = 4000)
    public void testReadLongFromByteArrayWithOffsetAndLength() throws Throwable {
        byte[] byteArray = new byte[9];
        byteArray[0] = (byte) 2;
        long result = ByteUtils.fromLittleEndian(byteArray, 0, 1);
        assertEquals(2L, result);
    }

    // Test for reading a long value from a byte array
    @Test(timeout = 4000)
    public void testReadLongFromByteArray() throws Throwable {
        byte[] byteArray = new byte[6];
        byteArray[2] = (byte) 46;
        long result = ByteUtils.fromLittleEndian(byteArray);
        assertEquals(3014656L, result);
    }

    // Test for reading a long value from a byte array with negative value
    @Test(timeout = 4000)
    public void testReadLongFromByteArrayWithNegativeValue() throws Throwable {
        byte[] byteArray = new byte[8];
        byteArray[7] = (byte) -1;
        long result = ByteUtils.fromLittleEndian(byteArray);
        assertEquals(-72057594037927936L, result);
    }

    // Test for reading a long value from a ByteSupplier with mocked values
    @Test(timeout = 4000)
    public void testReadLongFromMockedByteSupplier() throws Throwable {
        ByteUtils.ByteSupplier byteSupplier = mock(ByteUtils.ByteSupplier.class, new ViolatedAssumptionAnswer());
        doReturn(3148, 3148, 856, 3148, 3148).when(byteSupplier).getAsByte();
        long result = ByteUtils.fromLittleEndian(byteSupplier, 8);
        assertEquals(5497853135745207372L, result);
    }

    // Test for reading a long value from a ByteSupplier with negative mocked values
    @Test(timeout = 4000)
    public void testReadLongFromMockedByteSupplierWithNegativeValues() throws Throwable {
        ByteUtils.ByteSupplier byteSupplier = mock(ByteUtils.ByteSupplier.class, new ViolatedAssumptionAnswer());
        doReturn(2709, 2709, 2709, -1006, 2709).when(byteSupplier).getAsByte();
        long result = ByteUtils.fromLittleEndian(byteSupplier, 8);
        assertEquals(-12438233195L, result);
    }

    // Test for reading a long value from a ByteArrayInputStream with available bytes
    @Test(timeout = 4000)
    public void testReadLongFromByteArrayInputStreamWithAvailableBytes() throws Throwable {
        byte[] byteArray = new byte[6];
        byteArray[0] = (byte) 95;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        long result = ByteUtils.fromLittleEndian(byteArrayInputStream, 1);
        assertEquals(5, byteArrayInputStream.available());
        assertEquals(95L, result);
    }

    // Test for reading a long value from a DataInputStream with available bytes
    @Test(timeout = 4000)
    public void testReadLongFromDataInputStreamWithAvailableBytes2() throws Throwable {
        byte[] byteArray = new byte[5];
        byteArray[0] = (byte) 8;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        long result = ByteUtils.fromLittleEndian(dataInputStream, 1);
        assertEquals(4, byteArrayInputStream.available());
        assertEquals(8L, result);
    }

    // Test for handling NullPointerException with null byte array
    @Test(timeout = 4000)
    public void testNullPointerExceptionWithNullByteArray() throws Throwable {
        try {
            ByteUtils.toLittleEndian((byte[]) null, 0L, 182, 17);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    // Test for handling NullPointerException with null ByteConsumer
    @Test(timeout = 4000)
    public void testNullPointerExceptionWithNullByteConsumer() throws Throwable {
        try {
            ByteUtils.toLittleEndian((ByteUtils.ByteConsumer) null, 639L, 128);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    // Test for handling IOException with unconnected PipedOutputStream
    @Test(timeout = 4000)
    public void testIOExceptionWithUnconnectedPipedOutputStream() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        FilterOutputStream filterOutputStream = new FilterOutputStream(pipedOutputStream);
        ByteUtils.OutputStreamByteConsumer byteConsumer = new ByteUtils.OutputStreamByteConsumer(filterOutputStream);
        try {
            ByteUtils.toLittleEndian(byteConsumer, 0L, 2698);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedOutputStream", e);
        }
    }

    // Test for handling NullPointerException with null OutputStream
    @Test(timeout = 4000)
    public void testNullPointerExceptionWithNullOutputStream() throws Throwable {
        try {
            ByteUtils.toLittleEndian((OutputStream) null, 0L, 10);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    // Test for handling IOException with unconnected PipedOutputStream
    @Test(timeout = 4000)
    public void testIOExceptionWithUnconnectedPipedOutputStream2() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        try {
            ByteUtils.toLittleEndian(pipedOutputStream, 255L, 2857);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedOutputStream", e);
        }
    }

    // Test for handling NullPointerException with null byte array
    @Test(timeout = 4000)
    public void testNullPointerExceptionWithNullByteArray2() throws Throwable {
        try {
            ByteUtils.fromLittleEndian((byte[]) null, 55, 1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    // Test for handling NullPointerException with null byte array
    @Test(timeout = 4000)
    public void testNullPointerExceptionWithNullByteArray3() throws Throwable {
        try {
            ByteUtils.fromLittleEndian((byte[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    // Test for handling IllegalArgumentException with byte array length greater than eight
    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionWithByteArrayLengthGreaterThanEight() throws Throwable {
        byte[] byteArray = new byte[18];
        try {
            ByteUtils.fromLittleEndian(byteArray);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    // Test for handling IllegalArgumentException with ByteSupplier length greater than eight
    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionWithByteSupplierLengthGreaterThanEight() throws Throwable {
        try {
            ByteUtils.fromLittleEndian((ByteUtils.ByteSupplier) null, 190);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    // Test for handling IllegalArgumentException with InputStream length greater than eight
    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionWithInputStreamLengthGreaterThanEight() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        try {
            ByteUtils.fromLittleEndian(pipedInputStream, 1273);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    // Test for handling ArrayIndexOutOfBoundsException with ByteArrayInputStream
    @Test(timeout = 4000)
    public void testArrayIndexOutOfBoundsExceptionWithByteArrayInputStream() throws Throwable {
        byte[] byteArray = new byte[1];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray, -5477, 8);
        try {
            ByteUtils.fromLittleEndian(byteArrayInputStream, 1);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // No message in exception
        }
    }

    // Test for handling NullPointerException with null DataInput
    @Test(timeout = 4000)
    public void testNullPointerExceptionWithNullDataInput() throws Throwable {
        try {
            ByteUtils.fromLittleEndian((DataInput) null, 1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    // Test for handling IllegalArgumentException with DataInput length greater than eight
    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionWithDataInputLengthGreaterThanEight() throws Throwable {
        try {
            ByteUtils.fromLittleEndian((DataInput) null, 955);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    // Test for handling IOException with MockFileInputStream
    @Test(timeout = 4000)
    public void testIOExceptionWithMockFileInputStream() throws Throwable {
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileInputStream mockFileInputStream = new MockFileInputStream(fileDescriptor);
        DataInputStream dataInputStream = new DataInputStream(mockFileInputStream);
        try {
            ByteUtils.fromLittleEndian(dataInputStream, 2);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.evosuite.runtime.mock.java.io.NativeMockedIO", e);
        }
    }

    // Test for handling ArrayIndexOutOfBoundsException with byte array
    @Test(timeout = 4000)
    public void testArrayIndexOutOfBoundsExceptionWithByteArray() throws Throwable {
        byte[] byteArray = new byte[1];
        try {
            ByteUtils.fromLittleEndian(byteArray, 7, 7);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.compress.utils.ByteUtils", e);
        }
    }

    // Test for writing a long value to a FilterOutputStream in little-endian format
    @Test(timeout = 4000)
    public void testWriteLongToFilterOutputStream() throws Throwable {
        ByteArrayOutputStream byteArrayOutput