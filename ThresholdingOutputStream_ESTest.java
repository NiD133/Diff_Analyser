package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;
import org.apache.commons.io.output.DeferredFileOutputStream;
import org.apache.commons.io.output.ThresholdingOutputStream;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ThresholdingOutputStreamTest extends ThresholdingOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testThresholdNotExceeded() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(76);
        assertFalse(stream.isThresholdExceeded());
        assertEquals(76, stream.getThreshold());
    }

    @Test(timeout = 4000)
    public void testWriteNullArrayWithZeroThreshold() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(0);
        stream.write((byte[]) null, 0, 0);
        assertEquals(0, stream.getThreshold());
        assertEquals(0L, stream.getByteCount());
    }

    @Test(timeout = 4000)
    public void testThresholdReached() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(661);
        stream.thresholdReached();
        assertEquals(661, stream.getThreshold());
    }

    @Test(timeout = 4000)
    public void testGetThreshold() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(76);
        assertEquals(76, stream.getThreshold());
    }

    @Test(timeout = 4000)
    public void testNegativeThreshold() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(-2596);
        stream.getStream();
        assertEquals(0, stream.getThreshold());
    }

    @Test(timeout = 4000)
    public void testGetOutputStreamWithZeroThreshold() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(0);
        stream.getOutputStream();
        assertEquals(0, stream.getThreshold());
    }

    @Test(timeout = 4000)
    public void testWriteSingleByteArray() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(-2596);
        byte[] byteArray = new byte[1];
        stream.write(byteArray);
        assertEquals(1L, stream.getByteCount());
    }

    @Test(timeout = 4000)
    public void testDeferredFileOutputStreamWriteNullArrayThrowsNullPointerException() throws Throwable {
        DeferredFileOutputStream.Builder builder = new DeferredFileOutputStream.Builder();
        DeferredFileOutputStream stream = builder.get();
        try {
            stream.write((byte[]) null, 76, 76);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.nio.file.Files", e);
        }
    }

    @Test(timeout = 4000)
    public void testDeferredFileOutputStreamWriteNegativeIndexThrowsIndexOutOfBoundsException() throws Throwable {
        DeferredFileOutputStream.Builder builder = new DeferredFileOutputStream.Builder();
        DeferredFileOutputStream stream = builder.get();
        try {
            stream.write((byte[]) null, -122, -122);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.output.ByteArrayOutputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testDeferredFileOutputStreamInvalidPrefixThrowsIllegalArgumentException() throws Throwable {
        DeferredFileOutputStream.Builder builder = new DeferredFileOutputStream.Builder().setPrefix("/-f5");
        DeferredFileOutputStream stream = builder.get();
        try {
            stream.write((byte[]) null, -3053, 76);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.nio.file.TempFileHelper", e);
        }
    }

    @Test(timeout = 4000)
    public void testThresholdingOutputStreamWriteNullArrayThrowsNullPointerException() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(379);
        try {
            stream.write((byte[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.output.ThresholdingOutputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testDeferredFileOutputStreamWriteIntThrowsNullPointerException() throws Throwable {
        DeferredFileOutputStream.Builder builder = new DeferredFileOutputStream.Builder();
        DeferredFileOutputStream stream = builder.get();
        try {
            stream.write(0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.nio.file.Files", e);
        }
    }

    @Test(timeout = 4000)
    public void testDeferredFileOutputStreamThresholdReachedThrowsNullPointerException() throws Throwable {
        DeferredFileOutputStream.Builder builder = new DeferredFileOutputStream.Builder();
        DeferredFileOutputStream stream = builder.get();
        try {
            stream.thresholdReached();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.nio.file.Files", e);
        }
    }

    @Test(timeout = 4000)
    public void testDeferredFileOutputStreamCheckThresholdThrowsNullPointerException() throws Throwable {
        DeferredFileOutputStream.Builder builder = new DeferredFileOutputStream.Builder();
        DeferredFileOutputStream stream = builder.get();
        try {
            stream.checkThreshold(1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.nio.file.Files", e);
        }
    }

    @Test(timeout = 4000)
    public void testWriteByteArrayWithOffsetAndLength() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(-2596);
        byte[] byteArray = new byte[1];
        stream.write(byteArray);
        stream.write(byteArray, -1473, 1950);
        assertEquals(1951L, stream.getByteCount());
    }

    @Test(timeout = 4000)
    public void testFlushWithNegativeThreshold() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(-2596);
        stream.flush();
        assertEquals(0, stream.getThreshold());
    }

    @Test(timeout = 4000)
    public void testIsThresholdExceededAfterWrite() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(-2596);
        byte[] byteArray = new byte[1];
        stream.write(byteArray);
        assertTrue(stream.isThresholdExceeded());
        assertEquals(1L, stream.getByteCount());
    }

    @Test(timeout = 4000)
    public void testIsThresholdExceededWithoutWrite() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(-2596);
        assertFalse(stream.isThresholdExceeded());
        assertEquals(0, stream.getThreshold());
    }

    @Test(timeout = 4000)
    public void testConstructorWithConsumerAndFunction() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(1, null, null);
        assertEquals(1, stream.getThreshold());
    }

    @Test(timeout = 4000)
    public void testSetByteCount() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(129);
        stream.setByteCount(1);
        assertEquals(1L, stream.getByteCount());
    }

    @Test(timeout = 4000)
    public void testGetByteCount() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(-2596);
        assertEquals(0L, stream.getByteCount());
    }

    @Test(timeout = 4000)
    public void testResetByteCount() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(-2596);
        stream.resetByteCount();
        assertEquals(0L, stream.getByteCount());
        assertEquals(0, stream.getThreshold());
    }

    @Test(timeout = 4000)
    public void testWriteSingleByte() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(-2596);
        stream.write(345);
        assertEquals(1L, stream.getByteCount());
    }

    @Test(timeout = 4000)
    public void testCheckThresholdAfterWrite() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(-2596);
        byte[] byteArray = new byte[1];
        stream.write(byteArray);
        stream.checkThreshold(0);
        assertEquals(1L, stream.getByteCount());
    }

    @Test(timeout = 4000)
    public void testWriteWithNegativeOffsetAndLength() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(76);
        byte[] byteArray = new byte[3];
        stream.write(byteArray, -2302, -58);
        assertEquals(-58L, stream.getByteCount());
    }

    @Test(timeout = 4000)
    public void testClose() throws Throwable {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(76);
        stream.close();
        assertEquals(76, stream.getThreshold());
    }
}