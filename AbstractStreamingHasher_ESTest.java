package com.google.common.hash;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.hash.Crc32cHashFunction;
import com.google.common.hash.Hasher;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class AbstractStreamingHasher_ESTest extends AbstractStreamingHasher_ESTest_scaffolding {

    private static final int TIMEOUT = 4000;
    private static final int BYTE_BUFFER_SIZE = 2479;
    private static final int SMALL_BYTE_ARRAY_SIZE = 3;
    private static final int LARGE_BYTE_BUFFER_SIZE = 1905808397;

    @Test(timeout = TIMEOUT)
    public void testPutBytesWithCharsetEncoding() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        Charset charset = Charset.defaultCharset();
        ByteBuffer byteBuffer = charset.encode("maximum size was already set to %s");
        byteBuffer.getShort();
        Hasher resultHasher = hasher.putBytes(byteBuffer);
        assertSame(hasher, resultHasher);
    }

    @Test(timeout = TIMEOUT)
    public void testPutBytesWithIndexOutOfBoundsException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        byte[] byteArray = new byte[SMALL_BYTE_ARRAY_SIZE];
        try {
            hasher.putBytes(byteArray, 0, 67);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.nio.ByteBuffer", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutShortAfterHashThrowsBufferOverflowException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.hash();
        try {
            hasher.putShort((short) 24366);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutShortAfterProcessRemainingThrowsIllegalStateException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BYTE_BUFFER_SIZE);
        hasher.putBytes(byteBuffer).processRemaining(byteBuffer);
        try {
            hasher.putShort((short) -648);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.common.hash.Crc32cHashFunction$Crc32cHasher", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutLongAfterHashThrowsBufferOverflowException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.hash();
        try {
            hasher.putLong(-1479L);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutLongAfterMakeHashThrowsIllegalStateException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.makeHash();
        hasher.putLong(-4265267296055464877L);
        try {
            hasher.putLong(2304L);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.common.hash.Crc32cHashFunction$Crc32cHasher", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutIntAfterHashThrowsBufferOverflowException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.hash();
        try {
            hasher.putInt(1101871998);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutBytesAfterMakeHashThrowsIllegalStateException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(13);
        hasher.putBytes(byteBuffer);
        hasher.makeHash();
        try {
            hasher.putInt(13);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.common.hash.Crc32cHashFunction$Crc32cHasher", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutCharAfterHashThrowsBufferOverflowException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.hash();
        try {
            hasher.putChar('7');
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutCharAfterProcessRemainingThrowsIllegalStateException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BYTE_BUFFER_SIZE);
        hasher.putBytes(byteBuffer).processRemaining(byteBuffer);
        try {
            hasher.putChar('M');
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.common.hash.Crc32cHashFunction$Crc32cHasher", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutBytesAfterHashThrowsBufferOverflowException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.hash();
        byte[] byteArray = new byte[2];
        try {
            hasher.putBytes(byteArray, 1, 1);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutBytesWithNullArrayThrowsNullPointerException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        try {
            hasher.putBytes((byte[]) null, 703, 703);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.nio.HeapByteBuffer", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutBytesWithLargeBuffer() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(LARGE_BYTE_BUFFER_SIZE);
        hasher.putBytes(byteBuffer);
    }

    @Test(timeout = TIMEOUT)
    public void testPutBytesAfterHashWithBufferThrowsBufferOverflowException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.hash();
        ByteBuffer byteBuffer = ByteBuffer.allocate(80);
        try {
            hasher.putBytes(byteBuffer);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutBytesWithNullBufferThrowsNullPointerException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        try {
            hasher.putBytes((ByteBuffer) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.hash.AbstractStreamingHasher", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutBytesAfterMakeHashWithBufferThrowsIllegalStateException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.makeHash();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(185);
        try {
            hasher.putBytes(byteBuffer);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.common.hash.Crc32cHashFunction$Crc32cHasher", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutByteAfterHashThrowsBufferOverflowException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.hash();
        try {
            hasher.putByte((byte) -115);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testProcessRemainingWithLargeBuffer() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1317358741);
        hasher.processRemaining(byteBuffer);
    }

    @Test(timeout = TIMEOUT)
    public void testProcessRemainingWithNullBufferThrowsNullPointerException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        try {
            hasher.processRemaining((ByteBuffer) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.hash.Crc32cHashFunction$Crc32cHasher", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutBytesWithDirectBuffer() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        ByteBuffer byteBuffer = ByteBuffer.allocate(23);
        Hasher resultHasher = hasher.putBytes(byteBuffer);
        assertSame(hasher, resultHasher);
    }

    @Test(timeout = TIMEOUT)
    public void testPutBytesAfterHashWithVariousDataTypesThrowsIllegalStateException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.hash();
        byte[] byteArray = new byte[0];
        hasher.putBytes(byteArray);
        hasher.putBoolean(true);
        hasher.putChar('D');
        hasher.putInt(1347);
        hasher.putInt(7);
        hasher.putChar('D');
        hasher.putShort((short) -32506);
        try {
            hasher.putByte((byte) 21);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.common.hash.Crc32cHashFunction$Crc32cHasher", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutBytesWithCharsetEncodingAndBufferOverflowException() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        Charset charset = Charset.defaultCharset();
        ByteBuffer byteBuffer = charset.encode("maximum size was already set to %s");
        Crc32cHashFunction.Crc32cHasher resultHasher = (Crc32cHashFunction.Crc32cHasher) hasher.putBytes(byteBuffer);
        hasher.hash();
        byte[] byteArray = new byte[2];
        try {
            resultHasher.putBytes(byteArray, 1, 1);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPutBytesWithSmallArray() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        byte[] byteArray = new byte[2];
        Hasher resultHasher = hasher.putBytes(byteArray, 1, 1);
        assertSame(resultHasher, hasher);
    }

    @Test(timeout = TIMEOUT)
    public void testPutByte() throws Throwable {
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        Hasher resultHasher = hasher.putByte((byte) -115);
        assertSame(resultHasher, hasher);
    }
}