package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.io.ContentReference;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.MergedStream;
import com.fasterxml.jackson.core.util.BufferRecycler;
import java.io.*;
import java.util.Enumeration;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class MergedStreamTest extends MergedStream_ESTest_scaffolding {

    private static final int TIMEOUT = 4000;
    private static final int DEFAULT_BUFFER_SIZE = 8;
    private static final int LARGE_BUFFER_SIZE = 20000000;
    private static final int SMALL_BUFFER_SIZE = 2;
    private static final int NEGATIVE_INDEX = -1;
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int BYTE_98 = 98;

    @Test(timeout = TIMEOUT)
    public void testSkipNegativeBytesReturnsZero() throws Throwable {
        IOContext context = createDefaultIOContext(true);
        PipedInputStream pipedInputStream = new PipedInputStream(THREE);
        byte[] buffer = new byte[SMALL_BUFFER_SIZE];
        MergedStream mergedStream = new MergedStream(context, pipedInputStream, buffer, 255, 1000);
        MergedStream nestedMergedStream = new MergedStream(context, mergedStream, null, -2145635554, -2755);
        long skippedBytes = nestedMergedStream.skip(-838L);
        assertEquals(ZERO, skippedBytes);
    }

    @Test(timeout = TIMEOUT)
    public void testSkipPositiveBytes() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream(2914);
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, NEGATIVE_INDEX, 50000);
        MergedStream nestedMergedStream = new MergedStream(null, mergedStream, buffer, 255, 15);
        long skippedBytes = nestedMergedStream.skip(1261L);
        assertEquals(1261L, skippedBytes);
    }

    @Test(timeout = TIMEOUT)
    public void testSkipZeroBytes() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream(2914);
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, ZERO, ZERO);
        long skippedBytes = mergedStream.skip(ZERO);
        assertEquals(ZERO, skippedBytes);
    }

    @Test(timeout = TIMEOUT)
    public void testSkipOneByte() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream(2914);
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, ONE, 2914);
        long skippedBytes = mergedStream.skip(ONE);
        assertEquals(ONE, skippedBytes);
    }

    @Test(timeout = TIMEOUT)
    public void testReadReturnsMinusOneWhenNoMoreData() throws Throwable {
        IOContext context = createDefaultIOContext(true);
        Enumeration<FilterInputStream> enumeration = mockEnumeration(false);
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
        MergedStream mergedStream = new MergedStream(context, sequenceInputStream, null, 64, 256);
        int bytesRead = mergedStream.read(null, ZERO, 256);
        assertEquals(-1, bytesRead);
    }

    @Test(timeout = TIMEOUT)
    public void testReadSingleByte() throws Throwable {
        IOContext context = createDefaultIOContext(false);
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream, LARGE_BUFFER_SIZE);
        byte[] buffer = new byte[SMALL_BUFFER_SIZE];
        buffer[ZERO] = (byte) -25;
        MergedStream mergedStream = new MergedStream(context, pipedInputStream, buffer, ZERO, TWO);
        int byteRead = mergedStream.read();
        assertEquals(231, byteRead);
    }

    @Test(timeout = TIMEOUT)
    public void testReadReturnsZeroWhenNoMoreData() throws Throwable {
        Enumeration<FilterInputStream> enumeration = mockEnumeration(false);
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
        byte[] buffer = new byte[7];
        MergedStream mergedStream = new MergedStream(null, sequenceInputStream, buffer, ZERO, ZERO);
        int firstRead = mergedStream.read();
        assertEquals(ZERO, firstRead);

        int secondRead = mergedStream.read();
        assertEquals(-1, secondRead);
    }

    @Test(timeout = TIMEOUT)
    public void testAvailableBytes() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream(2914);
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, ONE, 2914);
        int availableBytes = mergedStream.available();
        assertEquals(2913, availableBytes);
    }

    @Test(timeout = TIMEOUT)
    public void testAvailableBytesWithNegativeEnd() throws Throwable {
        IOContext context = createDefaultIOContext(false);
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[5];
        MergedStream mergedStream = new MergedStream(context, pipedInputStream, buffer, TWO, -1357);
        int availableBytes = mergedStream.available();
        assertEquals(-1359, availableBytes);
    }

    @Test(timeout = TIMEOUT)
    public void testSkipThrowsNullPointerException() throws Throwable {
        IOContext context = createDefaultIOContext(false);
        byte[] buffer = new byte[ZERO];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(byteArrayInputStream);
        MergedStream mergedStream = new MergedStream(context, pushbackInputStream, buffer, 1000, 200);

        try {
            mergedStream.skip(-240L);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.io.IOContext", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadWithNegativeOffsetThrowsIndexOutOfBoundsException() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[12];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, ONE, ONE);
        mergedStream.read(buffer);

        try {
            mergedStream.read(buffer, -794, ZERO);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.io.PipedInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadWithNegativeLengthThrowsArrayIndexOutOfBoundsException() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[12];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, NEGATIVE_INDEX, NEGATIVE_INDEX);

        try {
            mergedStream.read(buffer, NEGATIVE_INDEX, NEGATIVE_INDEX);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadThrowsIOExceptionWhenPipeNotConnected() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[9];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, ONE, ONE);
        mergedStream.read(buffer, ONE, ONE);

        try {
            mergedStream.read(buffer, ONE, ONE);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadThrowsArrayIndexOutOfBoundsException() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[ONE];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, BYTE_98, BYTE_98);

        try {
            mergedStream.read(buffer);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadThrowsIOExceptionWhenPipeNotConnectedAgain() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[12];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, ONE, ONE);
        mergedStream.read(buffer);

        try {
            mergedStream.read(buffer);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadThrowsNullPointerException() throws Throwable {
        IOContext context = createDefaultIOContext(true);
        byte[] buffer = new byte[SMALL_BUFFER_SIZE];
        PipedInputStream pipedInputStream = new PipedInputStream();
        MergedStream mergedStream = new MergedStream(context, pipedInputStream, buffer, ONE, TWO);

        try {
            mergedStream.read();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.io.IOContext", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadThrowsArrayIndexOutOfBoundsExceptionAgain() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[ONE];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, BYTE_98, BYTE_98);

        try {
            mergedStream.read();
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.fasterxml.jackson.core.io.MergedStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testMarkSupportedThrowsNullPointerException() throws Throwable {
        IOContext context = createDefaultIOContext(true);
        MergedStream mergedStream = new MergedStream(context, null, null, 732, -2929);

        try {
            mergedStream.markSupported();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.io.MergedStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadWithZeroLengthReturnsZero() throws Throwable {
        IOContext context = createDefaultIOContext(false);
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream, LARGE_BUFFER_SIZE);
        byte[] buffer = new byte[SMALL_BUFFER_SIZE];
        MergedStream mergedStream = new MergedStream(context, pipedInputStream, buffer, ZERO, TWO);
        int bytesRead = mergedStream.read(buffer, ZERO, ZERO);
        assertEquals(ZERO, bytesRead);
    }

    @Test(timeout = TIMEOUT)
    public void testReadThrowsNullPointerExceptionAgain() throws Throwable {
        IOContext context = createDefaultIOContext(false);
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream, LARGE_BUFFER_SIZE);
        byte[] buffer = new byte[SMALL_BUFFER_SIZE];
        MergedStream mergedStream = new MergedStream(context, pipedInputStream, buffer, ZERO, TWO);

        try {
            mergedStream.read(buffer);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.io.IOContext", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testResetThrowsIOException() throws Throwable {
        IOContext context = createDefaultIOContext(true);
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream, LARGE_BUFFER_SIZE);
        MergedStream mergedStream = new MergedStream(context, pipedInputStream, null, 2447, TWO);

        try {
            mergedStream.reset();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.InputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReset() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[12];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, ONE, ONE);
        mergedStream.reset();
    }

    @Test(timeout = TIMEOUT)
    public void testReadSingleByteFromBuffer() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[ONE];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, ZERO, 97);
        int bytesRead = mergedStream.read(buffer);
        assertEquals(ONE, bytesRead);
    }

    @Test(timeout = TIMEOUT)
    public void testReadWithNullBufferThrowsNullPointerException() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, null, NEGATIVE_INDEX, NEGATIVE_INDEX);

        try {
            mergedStream.read(null, NEGATIVE_INDEX, NEGATIVE_INDEX);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.PipedInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadThrowsIOExceptionWhenPipeNotConnectedYetAgain() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[12];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, ONE, ONE);
        mergedStream.read(buffer, ONE, ONE);

        try {
            mergedStream.read();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testMarkSupportedReturnsTrue() throws Throwable {
        IOContext context = createDefaultIOContext(false);
        PipedInputStream pipedInputStream = new PipedInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(pipedInputStream);
        MergedStream mergedStream = new MergedStream(context, bufferedInputStream, null, 65536, THREE);
        boolean markSupported = mergedStream.markSupported();
        assertTrue(markSupported);
    }

    @Test(timeout = TIMEOUT)
    public void testMarkSupportedReturnsFalse() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, null, NEGATIVE_INDEX, NEGATIVE_INDEX);
        boolean markSupported = mergedStream.markSupported();
        assertFalse(markSupported);
    }

    @Test(timeout = TIMEOUT)
    public void testMarkSupportedReturnsFalseAgain() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[20];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, BYTE_98, BYTE_98);
        boolean markSupported = mergedStream.markSupported();
        assertFalse(markSupported);
    }

    @Test(timeout = TIMEOUT)
    public void testMark() throws Throwable {
        IOContext context = createDefaultIOContext(true);
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream, 500);
        MergedStream mergedStream = new MergedStream(context, pipedInputStream, null, 2541, TWO);
        mergedStream.mark(ZERO);
    }

    @Test(timeout = TIMEOUT)
    public void testMarkWithNegativeValue() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[ONE];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, -629, -629);
        mergedStream.mark(-629);
    }

    @Test(timeout = TIMEOUT)
    public void testAvailableReturnsZero() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[ONE];
        MergedStream mergedStream = new MergedStream(null, pipedInputStream, buffer, ONE, ONE);
        int availableBytes = mergedStream.available();
        assertEquals(ZERO, availableBytes);
    }

    @Test(timeout = TIMEOUT)
    public void testAvailableReturnsZeroWithMockFile() throws Throwable {
        IOContext context = createDefaultIOContext(false);
        File file = MockFile.createTempFile("8`3ZeQ", "8`3ZeQ");
        MockFileInputStream mockFileInputStream = new MockFileInputStream(file);
        MergedStream mergedStream = new MergedStream(context, mockFileInputStream, null, 50000, ZERO);
        int availableBytes = mergedStream.available();
        assertEquals(ZERO, availableBytes);
    }

    @Test(timeout = TIMEOUT)
    public void testCloseThrowsNullPointerException() throws Throwable {
        byte[] buffer = new byte[THREE];
        MergedStream mergedStream = new MergedStream(null, null, buffer, ZERO, BYTE_98);

        try {
            mergedStream.close();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.io.MergedStream", e);
        }
    }

    private IOContext createDefaultIOContext(boolean managedResource) {
        StreamReadConstraints readConstraints = StreamReadConstraints.defaults();
        StreamWriteConstraints writeConstraints = StreamWriteConstraints.defaults();
        ErrorReportConfiguration errorConfig = ErrorReportConfiguration.defaults();
        BufferRecycler bufferRecycler = new BufferRecycler();
        ContentReference contentReference = ContentReference.rawReference(true, readConstraints);
        return new IOContext(readConstraints, writeConstraints, errorConfig, bufferRecycler, contentReference, managedResource);
    }

    private Enumeration<FilterInputStream> mockEnumeration(boolean hasMoreElements) {
        Enumeration<FilterInputStream> enumeration = mock(Enumeration.class, new ViolatedAssumptionAnswer());
        doReturn(hasMoreElements).when(enumeration).hasMoreElements();
        return enumeration;
    }
}